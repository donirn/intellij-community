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

/**
 * Created by Mushfiqur on 5/9/2016.
 */

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.psi.codeStyle.CodeStyleScheme;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

import javax.swing.UIManager.LookAndFeelInfo;

public class QuickSchemeModel implements FsmModel {

  private enum State {Initialization, StandBy, CheckCS, CheckCSS, CheckKM, CheckLAF, CheckPM, CheckDFM}
  private State state = State.Initialization;

  private QuickSchemeAdapter qsAdapter;

  //TODO declare all the internal variable here
  private EditorColorsScheme currentCS, selectedCS;
  private CodeStyleScheme currentCSS, selectedCSS;
  private Keymap currentKM, selectedKM;
  private boolean currentDFM, selectedDFM;
  private boolean currentPM, selectedPM;
  private LookAndFeelInfo currentLaf, selectedLaf;

  public QuickSchemeModel() throws Exception{
    this.qsAdapter = new QuickSchemeAdapter();
  }

  public Object getState() {
    return state;
  }

  public void reset(boolean b) {
    state = State.Initialization;

    currentCS = null;
    currentCSS = null;
    currentKM = null;
    currentDFM = false;
    currentPM = false;
    currentLaf = null;

    selectedCS = null;
    selectedCSS = null;
    selectedKM = null;
    selectedDFM = false;
    selectedPM = false;
    selectedLaf = null;
  }

  public boolean setDefaultGuard()
  {return state == State.Initialization;}

  @Action
  public void setDefault() {
    state = State.StandBy;

    // TODO get all the currentVars here
    currentCS = qsAdapter.getCurrentCS();
    currentCSS = qsAdapter.getCurrentCSS();
    currentKM = qsAdapter.getCurrentKM();
    currentDFM = qsAdapter.getCurrentDFM();
    currentPM = qsAdapter.getCurrentPM();
    currentLaf = qsAdapter.getCurrentLAF();
  }

  public boolean selectCSGuard()
  {return state == State.StandBy;}


  @Action
  public void selectCS() {
    state = State.CheckCS;
    selectedCS = qsAdapter.selectCS();
  }

  public boolean selectCSSGuard()
  {return state == State.StandBy;}

  @Action
  public void selectCSS() {
    state = State.CheckCSS;
    selectedCSS = qsAdapter.selectCSS();
  }

  public boolean selectKMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectKM() {
    state = State.CheckKM;
    selectedKM = qsAdapter.selectKM();
  }

  public boolean selectLAFGuard()
  {return state == State.StandBy;}

  @Action
  public void selectLAF() {
    state = State.CheckLAF;
    selectedLaf = qsAdapter.selectLAF();
  }

  public boolean selectPMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectPM() {
    state = State.CheckPM;
    selectedPM = qsAdapter.selectPM();
  }

  public boolean selectDFMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectDFM() {
    state = State.CheckDFM;
    selectedDFM = qsAdapter.selectDFM();
  }

  public boolean changedCSGuard()
  {return (state == State.CheckCS) && (currentCS != selectedCS) ;}

  @Action
  public void changedCS() {
    state = State.StandBy;
    qsAdapter.changedCS(selectedCS);
    currentCS = selectedCS;
  }

  public boolean notChangedCSGuard()
  {return (state == State.CheckCS) && (currentCS == selectedCS);}

  @Action
  public void notChangedCS() {
    state = State.StandBy;
    qsAdapter.notChangedCS(selectedCS);
  }

  public boolean changedCSSGuard()
  {return (state == State.CheckCSS) && (currentCSS != selectedCSS);}

  @Action
  public void changedCSS() {
    state = State.StandBy;
    qsAdapter.changedCSS(selectedCSS);
    currentCSS = selectedCSS;
  }

  public boolean notChangedCSSGuard()
  {return (state == State.CheckCSS) && (currentCSS == selectedCSS);}

  @Action
  public void notChangedCSS() {
    state = State.StandBy;
    qsAdapter.notChangedCSS(selectedCSS);
  }

  public boolean changedKMGuard()
  {return (state == State.CheckKM) && (currentKM != selectedKM);}

  @Action
  public void changedKM() {
    state = State.StandBy;
    qsAdapter.changedKM(selectedKM);
    currentKM = selectedKM;
  }

  public boolean notChangedKMGuard()
  {return (state == State.CheckKM) && (currentKM == selectedKM);}

  @Action
  public void notChangedKM() {
    state = State.StandBy;
    qsAdapter.notChangedKM(selectedKM);
  }

  public boolean changedLAFGuard()
  {return (state == State.CheckLAF) && (currentLaf != selectedLaf);}

  @Action
  public void changedLAF() {
    state = State.StandBy;
    qsAdapter.changedLAF(selectedLaf);
    currentLaf = selectedLaf;
  }

  public boolean notChangedLAFGuard()
  {return (state == State.CheckLAF) && (currentLaf == selectedLaf);}

  @Action
  public void notChangedLAF() {
    state = State.StandBy;
    qsAdapter.notChangedLAF(selectedLaf);
  }

  public boolean changedPMGuard()
  {return (state == State.CheckPM) && (currentPM != selectedPM);}

  @Action
  public void changedPM() {
    state = State.StandBy;
    qsAdapter.changedPM(selectedPM);
    currentPM = selectedPM;
  }

  public boolean notChangedPMGuard()
  {return (state == State.CheckPM) && (currentPM == selectedPM);}

  @Action
  public void notChangedPM() {
    state = State.StandBy;
    qsAdapter.notChangedPM(selectedPM);
  }

  public boolean changedDFMGuard()
  {return (state == State.CheckDFM) && (currentDFM != selectedDFM);}

  @Action
  public void changedDFM() {
    state = State.StandBy;
    qsAdapter.changedDFM(selectedDFM);
    currentDFM = selectedDFM;
  }

  public boolean notChangedDFMGuard()
  {return (state == State.CheckDFM) && (currentDFM == selectedDFM);}

  @Action
  public void notChangedDFM() {
    state = State.StandBy;
    qsAdapter.notChangedDFM(selectedDFM);
  }

  public void cleanup() throws Exception {
    qsAdapter.cleanup();
  }
}
