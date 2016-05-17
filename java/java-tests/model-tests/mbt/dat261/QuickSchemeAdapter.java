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
package mbt.dat261;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings;
import com.intellij.codeInsight.daemon.LightIntentionActionTestCase;
import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorSettingsExternalizable;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.registry.RegistryValue;
import com.intellij.psi.codeStyle.CodeStyleScheme;
import com.intellij.psi.codeStyle.CodeStyleSchemes;
import com.intellij.psi.impl.source.codeStyle.CodeStyleSchemesImpl;

import javax.swing.UIManager.LookAndFeelInfo;
import java.util.List;
import java.util.Random;

import static com.intellij.ide.actions.ToggleDistractionFreeModeAction.applyAndSave;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by doniramadhan on 16/05/16.
 */

@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class QuickSchemeAdapter extends LightIntentionActionTestCase {
  private final Random randomGenerator = new Random();
  public QuickSchemeAdapter() throws Exception {
    super();
    super.setUp();
    //configureByFile("/model-based/DeadCode.java");
  }


// CSS : Code Style Scheme

  public CodeStyleScheme getCurrentCSS(){
    return CodeStyleSchemes.getInstance().getCurrentScheme();
  }

  public CodeStyleScheme selectCSS(){
    List<CodeStyleScheme> schemes = CodeStyleSchemesImpl.getSchemeManager().getAllSchemes();

    int size = schemes.size();
    int randomNumber = randomGenerator.nextInt(size);

    return schemes.get(randomNumber);
  }

  public void changedCSS(CodeStyleScheme scheme){
    CodeStyleScheme prevScheme = CodeStyleSchemes.getInstance().getCurrentScheme();
    CodeStyleSchemes.getInstance().setCurrentScheme(scheme);
    CodeStyleScheme currScheme = CodeStyleSchemes.getInstance().getCurrentScheme();

    assertThat(currScheme, is(scheme));
    assertThat(currScheme, is(not(prevScheme)));
  }

  public void notChangedCSS(CodeStyleScheme scheme){
    CodeStyleScheme prevScheme = CodeStyleSchemes.getInstance().getCurrentScheme();
    CodeStyleSchemes.getInstance().setCurrentScheme(scheme);
    CodeStyleScheme currScheme = CodeStyleSchemes.getInstance().getCurrentScheme();

    assertThat(currScheme, is(scheme));
    assertThat(currScheme, is(prevScheme));
  }

// CS : Color Scheme

  public EditorColorsScheme getCurrentCS(){
    return EditorColorsManager.getInstance().getGlobalScheme();
  }

  public EditorColorsScheme selectCS(){
    EditorColorsScheme[] schemes = EditorColorsManager.getInstance().getAllSchemes();

    int size = schemes.length;
    int randomNumber = randomGenerator.nextInt(size);

    return schemes[randomNumber];
  }

  public void setCS(EditorColorsScheme scheme){
    EditorColorsManager.getInstance().setGlobalScheme(scheme);
  }

// KM : Keymap

  public Keymap getCurrentKM(){
    return KeymapManager.getInstance().getActiveKeymap();
  }

  public Keymap selectKM(){
    Keymap[] keymaps = ((KeymapManagerEx)KeymapManager.getInstance()).getAllKeymaps();

    int size = keymaps.length;
    int randomNumber = randomGenerator.nextInt(size);

    return keymaps[randomNumber];
  }

  public void changedKM(Keymap keymap){
    KeymapManagerEx keymapManagerEx = (KeymapManagerEx)KeymapManager.getInstance();
    Keymap prevKeymap = keymapManagerEx.getActiveKeymap();
    keymapManagerEx.setActiveKeymap(keymap);
    Keymap currKeymap = keymapManagerEx.getActiveKeymap();;

    assertThat(currKeymap, is(keymap));
    assertThat(currKeymap, is(not(prevKeymap)));
  }

  public void notChangedKM(Keymap keymap){
    KeymapManagerEx keymapManagerEx = (KeymapManagerEx)KeymapManager.getInstance();
    Keymap prevKeymap = keymapManagerEx.getActiveKeymap();
    keymapManagerEx.setActiveKeymap(keymap);
    Keymap currKeymap = keymapManagerEx.getActiveKeymap();;

    assertThat(currKeymap, is(keymap));
    assertThat(currKeymap, is(prevKeymap));
  }

// DFM : Distraction Free Mode
  private static final String DFM_KEY = "editor.distraction.free.mode";

  public boolean getCurrentDFM(){
    RegistryValue value = Registry.get(DFM_KEY);
    return value.asBoolean();
  }

  public boolean selectDFM(){
    int randomNumber = randomGenerator.nextInt(2);
    return randomNumber > 0.5;
  }

  public void changedDFM(boolean dfm){
    boolean prevDFM = Registry.get(DFM_KEY).asBoolean();
    setDFM(dfm);
    boolean currDFM = Registry.get(DFM_KEY).asBoolean();

    assertThat(currDFM, is(dfm));
    assertThat(currDFM, is(not(prevDFM)));
  }

  public void notChangedDFM(boolean dfm){
    boolean prevDFM = Registry.get(DFM_KEY).asBoolean();
    setDFM(dfm);
    boolean currDFM = Registry.get(DFM_KEY).asBoolean();

    assertThat(currDFM, is(dfm));
    assertThat(currDFM, is(prevDFM));
  }

  private void setDFM(boolean dfm){
    Project project = getProject();
    RegistryValue value = Registry.get(DFM_KEY);
    value.setValue(dfm);

    if (project == null) return;

    PropertiesComponent p = PropertiesComponent.getInstance();
    UISettings ui = UISettings.getInstance();
    EditorSettingsExternalizable.OptionSet eo = EditorSettingsExternalizable.getInstance().getOptions();
    DaemonCodeAnalyzerSettings ds = DaemonCodeAnalyzerSettings.getInstance();

    String before = "BEFORE.DISTRACTION.MODE.";
    String after = "AFTER.DISTRACTION.MODE.";
    if (dfm) {
      applyAndSave(p, ui, eo, ds, before, after, false);
      //TogglePresentationModeAction.storeToolWindows(project);
    }
    else {
      applyAndSave(p, ui, eo, ds, after, before, true);
      //TogglePresentationModeAction.restoreToolWindows(project, true, false);
    }

    UISettings.getInstance().fireUISettingsChanged();
    LafManager.getInstance().updateUI();
    EditorUtil.reinitSettings();
    DaemonCodeAnalyzer.getInstance(project).settingsChanged();
    EditorFactory.getInstance().refreshAllEditors();
  }

  // LAF : Look And Feel

  public LookAndFeelInfo getCurrentLAF(){
    return LafManager.getInstance().getCurrentLookAndFeel();
  }

  public LookAndFeelInfo selectLAF(){
    LookAndFeelInfo[] lafs = LafManager.getInstance().getInstalledLookAndFeels();

    int size = lafs.length;
    if(size == 0) return null;
    int randomNumber = randomGenerator.nextInt(size);

    return lafs[randomNumber];
  }

  public void changedLAF(LookAndFeelInfo laf){
    LookAndFeelInfo prevLaf = LafManager.getInstance().getCurrentLookAndFeel();
    LafManager.getInstance().setCurrentLookAndFeel(laf);
    LookAndFeelInfo currLaf = LafManager.getInstance().getCurrentLookAndFeel();

    assertThat(currLaf, is(laf));
    assertThat(currLaf, is(not(prevLaf)));
  }

  public void notChangedLAF(LookAndFeelInfo laf){
    LookAndFeelInfo prevLaf = LafManager.getInstance().getCurrentLookAndFeel();
    LafManager.getInstance().setCurrentLookAndFeel(laf);
    LookAndFeelInfo currLaf = LafManager.getInstance().getCurrentLookAndFeel();

    assertThat(currLaf, is(laf));
    assertThat(currLaf, is(prevLaf));
  }

// LightIntentionActionTestCase related codes

  @Override
  public String getName() {
    return "testQuickScheme";
  }

  public void cleanup() throws Exception {
    tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will prevent tests from running green.
  }
}
