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

  public CodeStyleScheme getCurrentCSS(){
    return CodeStyleSchemes.getInstance().getCurrentScheme();
  }

  @Override
  public String getName() {
    return "testQuickScheme";
  }

  public void cleanup() throws Exception {
    tearDown(); // Otherwise IntelliJ will throw an exception regarding memory leakage, which will prevent tests from running green.
  }
}
