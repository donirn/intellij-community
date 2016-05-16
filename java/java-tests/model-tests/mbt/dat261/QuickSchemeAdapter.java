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
import java.util.Random;

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

  public CodeStyleScheme getRandomCSS(){
    List<CodeStyleScheme> schemes = CodeStyleSchemesImpl.getSchemeManager().getAllSchemes();

    int size = schemes.size();
    int randomNumber = randomGenerator.nextInt(size);

    return schemes.get(randomNumber);
  }

  public void setCSS(CodeStyleScheme scheme){
    CodeStyleSchemes.getInstance().setCurrentScheme(scheme);
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
