/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.vcs.changes.patch.tool;

import com.intellij.diff.comparison.ByWord;
import com.intellij.diff.comparison.ComparisonPolicy;
import com.intellij.diff.comparison.DiffTooBigException;
import com.intellij.diff.fragments.DiffFragment;
import com.intellij.diff.merge.MergeModelBase;
import com.intellij.diff.util.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diff.DiffBundle;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.progress.DumbProgressIndicator;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vcs.changes.patch.AppliedTextPatch.HunkStatus;
import com.intellij.openapi.vcs.ex.LineStatusMarkerRenderer;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.PairConsumer;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class ApplyPatchChange {
  @NotNull private final ApplyPatchViewer myViewer;
  private final int myIndex; // index in myModelChanges

  @NotNull private final LineRange myPatchDeletionRange;
  @NotNull private final LineRange myPatchInsertionRange;
  @NotNull private final HunkStatus myStatus;

  @Nullable private final List<DiffFragment> myPatchInnerDifferences;
  @NotNull private final List<MyGutterOperation> myOperations = new ArrayList<>();

  @NotNull private final List<RangeHighlighter> myHighlighters = new ArrayList<>();

  private boolean myResolved;

  public ApplyPatchChange(@NotNull PatchChangeBuilder.Hunk hunk, int index, @NotNull ApplyPatchViewer viewer) {
    myIndex = index;
    myViewer = viewer;
    myPatchDeletionRange = hunk.getPatchDeletionRange();
    myPatchInsertionRange = hunk.getPatchInsertionRange();
    myStatus = hunk.getStatus();

    myPatchInnerDifferences = calcPatchInnerDifferences(hunk, viewer);
  }

  @Nullable
  private static List<DiffFragment> calcPatchInnerDifferences(@NotNull PatchChangeBuilder.Hunk hunk,
                                                              @NotNull ApplyPatchViewer viewer) {
    LineRange deletionRange = hunk.getPatchDeletionRange();
    LineRange insertionRange = hunk.getPatchInsertionRange();

    if (deletionRange.isEmpty() || insertionRange.isEmpty()) return null;

    try {
      DocumentEx patchDocument = viewer.getPatchEditor().getDocument();
      CharSequence deleted = DiffUtil.getLinesContent(patchDocument, deletionRange.start, deletionRange.end);
      CharSequence inserted = DiffUtil.getLinesContent(patchDocument, insertionRange.start, insertionRange.end);

      return ByWord.compare(deleted, inserted, ComparisonPolicy.DEFAULT, DumbProgressIndicator.INSTANCE);
    }
    catch (DiffTooBigException ignore) {
      return null;
    }
  }

  public void reinstallHighlighters() {
    destroyHighlighters();
    installHighlighters();

    myViewer.repaintDivider();
  }

  private void installHighlighters() {
    createResultHighlighters();
    createPatchHighlighters();
    createStatusHighlighter();
    createOperations();
  }

  private void createPatchHighlighters() {
    EditorEx patchEditor = myViewer.getPatchEditor();
    myHighlighters.addAll(DiffDrawUtil.createUnifiedChunkHighlighters(patchEditor, myPatchDeletionRange, myPatchInsertionRange,
                                                                      myPatchInnerDifferences));
  }

  private void createResultHighlighters() {
    LineRange resultRange = getResultRange();
    if (resultRange == null) return;
    EditorEx editor = myViewer.getResultEditor();

    int startLine = resultRange.start;
    int endLine = resultRange.end;

    TextDiffType type = getDiffType();
    boolean resolved = isRangeApplied();

    myHighlighters.addAll(DiffDrawUtil.createHighlighter(editor, startLine, endLine, type, false, resolved, false));
    myHighlighters.addAll(DiffDrawUtil.createLineMarker(editor, startLine, endLine, type, resolved));
  }

  private void createStatusHighlighter() {
    int line1 = myPatchDeletionRange.start;
    int line2 = myPatchInsertionRange.end;

    Color color = getStatusColor();
    if (isResolved()) {
      color = ColorUtil.mix(color, myViewer.getPatchEditor().getGutterComponentEx().getBackground(), 0.6f);
    }

    String tooltip = getStatusText();

    EditorEx patchEditor = myViewer.getPatchEditor();
    Document document = patchEditor.getDocument();
    MarkupModelEx markupModel = patchEditor.getMarkupModel();
    TextRange textRange = DiffUtil.getLinesRange(document, line1, line2);

    RangeHighlighter highlighter = markupModel.addRangeHighlighter(textRange.getStartOffset(), textRange.getEndOffset(),
                                                                   HighlighterLayer.LAST, null, HighlighterTargetArea.LINES_IN_RANGE);

    PairConsumer<Editor, MouseEvent> clickHandler = getResultRange() != null ?
                                                    (e, event) -> myViewer.scrollToChange(this, Side.RIGHT, false) :
                                                    null;
    highlighter.setLineMarkerRenderer(LineStatusMarkerRenderer.createRenderer(line1, line2, color, tooltip, clickHandler));

    myHighlighters.add(highlighter);
  }

  private void destroyHighlighters() {
    for (RangeHighlighter highlighter : myHighlighters) {
      highlighter.dispose();
    }
    myHighlighters.clear();

    for (MyGutterOperation operation : myOperations) {
      operation.dispose();
    }
    myOperations.clear();
  }

  //
  // Getters
  //

  public int getIndex() {
    return myIndex;
  }

  @NotNull
  public HunkStatus getStatus() {
    return myStatus;
  }

  @NotNull
  public LineRange getPatchRange() {
    return new LineRange(myPatchDeletionRange.start, myPatchInsertionRange.end);
  }

  @NotNull
  public LineRange getPatchAffectedRange() {
    return isRangeApplied() ? myPatchInsertionRange : myPatchDeletionRange;
  }

  @NotNull
  public LineRange getPatchDeletionRange() {
    return myPatchDeletionRange;
  }

  @NotNull
  public LineRange getPatchInsertionRange() {
    return myPatchInsertionRange;
  }

  @Nullable
  public LineRange getResultRange() {
    ApplyPatchViewer.MyModel model = myViewer.getModel();
    int lineStart = model.getLineStart(myIndex);
    int lineEnd = model.getLineEnd(myIndex);

    if (lineStart != -1 || lineEnd != -1) return new LineRange(lineStart, lineEnd);
    return null;
  }

  public boolean isResolved() {
    return myResolved;
  }

  public void setResolved(boolean resolved) {
    myResolved = resolved;
  }

  @NotNull
  public TextDiffType getDiffType() {
    return DiffUtil.getDiffType(!myPatchDeletionRange.isEmpty(), !myPatchInsertionRange.isEmpty());
  }

  public boolean isRangeApplied() {
    return myResolved || getStatus() == HunkStatus.ALREADY_APPLIED;
  }

  @NotNull
  private String getStatusText() {
    switch (myStatus) {
      case ALREADY_APPLIED:
        return "Already applied";
      case EXACTLY_APPLIED:
        return "Automatically applied";
      case NOT_APPLIED:
        return "Not applied";
      default:
        throw new IllegalStateException();
    }
  }

  @NotNull
  private Color getStatusColor() {
    switch (myStatus) {
      case ALREADY_APPLIED:
        return JBColor.YELLOW.darker();
      case EXACTLY_APPLIED:
        return JBColor.BLUE.darker();
      case NOT_APPLIED:
        return JBColor.RED.darker();
      default:
        throw new IllegalStateException();
    }
  }

  //
  // Operations
  //

  private void createOperations() {
    if (myViewer.isReadOnly()) return;
    if (isResolved()) return;

    if (myStatus == HunkStatus.EXACTLY_APPLIED) {
      ContainerUtil.addIfNotNull(myOperations, createOperation(OperationType.APPLY));
    }
    ContainerUtil.addIfNotNull(myOperations, createOperation(OperationType.IGNORE));
  }

  @Nullable
  private MyGutterOperation createOperation(@NotNull OperationType type) {
    if (isResolved()) return null;

    EditorEx editor = myViewer.getPatchEditor();
    Document document = editor.getDocument();

    int line = getPatchRange().start;
    int offset = line == DiffUtil.getLineCount(document) ? document.getTextLength() : document.getLineStartOffset(line);

    RangeHighlighter highlighter = editor.getMarkupModel().addRangeHighlighter(offset, offset,
                                                                               HighlighterLayer.ADDITIONAL_SYNTAX,
                                                                               null,
                                                                               HighlighterTargetArea.LINES_IN_RANGE);
    return new MyGutterOperation(highlighter, type);
  }

  private class MyGutterOperation {
    @NotNull private final RangeHighlighter myHighlighter;
    @NotNull private final OperationType myType;

    private MyGutterOperation(@NotNull RangeHighlighter highlighter, @NotNull OperationType type) {
      myHighlighter = highlighter;
      myType = type;

      myHighlighter.setGutterIconRenderer(createRenderer());
    }

    public void dispose() {
      myHighlighter.dispose();
    }

    @Nullable
    public GutterIconRenderer createRenderer() {
      switch (myType) {
        case APPLY:
          return createApplyRenderer();
        case IGNORE:
          return createIgnoreRenderer();
        default:
          throw new IllegalArgumentException(myType.name());
      }
    }
  }

  @Nullable
  private GutterIconRenderer createApplyRenderer() {
    return createIconRenderer(DiffBundle.message("merge.dialog.apply.change.action.name"), DiffUtil.getArrowIcon(Side.RIGHT), () -> {
      myViewer.executeCommand("Accept change", () -> {
        myViewer.replaceChange(this);
      });
    });
  }

  @Nullable
  private GutterIconRenderer createIgnoreRenderer() {
    return createIconRenderer(DiffBundle.message("merge.dialog.ignore.change.action.name"), AllIcons.Diff.Remove, () -> {
      myViewer.executeCommand("Ignore change", () -> {
        myViewer.markChangeResolved(this);
      });
    });
  }

  @Nullable
  private static GutterIconRenderer createIconRenderer(@NotNull final String text,
                                                       @NotNull final Icon icon,
                                                       @NotNull final Runnable perform) {
    final String tooltipText = DiffUtil.createTooltipText(text, null);
    return new DiffGutterRenderer(icon, tooltipText) {
      @Override
      protected void performAction(AnActionEvent e) {
        perform.run();
      }
    };
  }

  private enum OperationType {
    APPLY, IGNORE
  }

  //
  // State
  //

  @NotNull
  public State storeState() {
    LineRange resultRange = getResultRange();
    return new State(
      myIndex,
      resultRange != null ? resultRange.start : -1,
      resultRange != null ? resultRange.end : -1,
      myResolved);
  }

  public void restoreState(@NotNull State state) {
    myResolved = state.myResolved;
  }

  public static class State extends MergeModelBase.State {
    private final boolean myResolved;

    public State(int index,
                 int startLine,
                 int endLine,
                 boolean resolved) {
      super(index, startLine, endLine);
      myResolved = resolved;
    }
  }
}
