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

import com.intellij.codeInsight.daemon.LightIntentionActionTestCase;
import com.intellij.psi.codeStyle.CodeStyleScheme;
import com.intellij.psi.codeStyle.CodeStyleSchemes;
import com.intellij.psi.impl.source.codeStyle.CodeStyleSchemesImpl;

import java.util.List;

/**
 * Created by doniramadhan on 16/05/16.
 */

@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class QuickSchemeAdapter extends LightIntentionActionTestCase {
  public QuickSchemeAdapter() throws Exception {
    super();
    super.setUp();
    //configureByFile("/model-based/DeadCode.java");
  }

  //public void addDeadCode() {
  //  type("int b = 3;\n");
  //
  //  up();
  //  myEditor.getCaretModel().getCurrentCaret().selectLineAtCaret();
  //  String actualLine = myEditor.getCaretModel().getCurrentCaret().getSelectedText();
  //  down();
  //
  //  String expectedLine = "      int b = 3;";
  //
  //  // To see an error, you can use the below assertion. Causes
  //  //    TestFailureException: failure in action addDeadCode from state NoDeadCode due to junit.framework.ComparisonFailure: expected:<int b = 3;[
  //  //    ]> but was:<int b = 3;[]>
  //  // I.e. there's also a linebreak at the end of file.
  //  //assertEquals(expectedLine, actualLine);
  //
  //  assertEquals(expectedLine + "\n", actualLine); // works
  //
  //  // use debugger and Alt+F8 to try out carret movement interactively
  //  up();
  //  right();
  //  myEditor.getCaretModel().getCurrentCaret().selectWordAtCaret(false);
  //
  //  String actualWord = myEditor.getCaretModel().getCurrentCaret().getSelectedText();
  //  String expectedWord = "int";
  //  assertEquals(expectedWord, actualWord);
  //}

  public void testSetScheme(){
    CodeStyleSchemes schemes = CodeStyleSchemes.getInstance();
    CodeStyleScheme scheme = schemes.getCurrentScheme();
    schemes.setCurrentScheme(scheme);

    List<CodeStyleScheme> schemesList = CodeStyleSchemesImpl.getSchemeManager().getAllSchemes();
    return;
  }

  @Override
  public String getName() {
    return "testQuickScheme";
  }

  public void cleanup() throws Exception {
    tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will prevent tests from running green.
  }
}
