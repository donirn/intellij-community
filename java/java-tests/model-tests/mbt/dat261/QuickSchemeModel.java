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

import com.intellij.psi.codeStyle.CodeStyleScheme;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

public class QuickSchemeModel implements FsmModel {

  private enum State {Initialization, StandBy, CheckCS, CheckCSS, CheckKM, CheckLAF, CheckPM, CheckFS, CheckDFM}
  private State state = State.Initialization;

  private QuickSchemeAdapter qsAdapter;

  //TODO declare all the internal variable here
  private CodeStyleScheme currentCSS, selectedCSS;

  public QuickSchemeModel() throws Exception{
    this.qsAdapter = new QuickSchemeAdapter();
  }

  public Object getState() {
    return state;
  }

  public void reset(boolean b) {
    state = State.Initialization;
  }

  public boolean setDefaultGuard()
  {return state == State.Initialization;}

  @Action
  public void setDefault() {
    state = State.StandBy;

    // TODO get all the currentVars here
    currentCSS = qsAdapter.getCurrentCSS();
  }

  public boolean selectCSGuard()
  {return state == State.StandBy;}


  @Action
  public void selectCS() {
    state = State.CheckCS;
  }

  public boolean selectCSSGuard()
  {return state == State.StandBy;}

  @Action
  public void selectCSS() {
    state = State.CheckCSS;
    selectedCSS = qsAdapter.getRandomCSS();
  }

  public boolean selectKMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectKM() {
    state = State.CheckKM;
  }

  public boolean selectLAFGuard()
  {return state == State.StandBy;}

  @Action
  public void selectLAF() {
    state = State.CheckLAF;
  }

  public boolean selectPMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectPM() {
    state = State.CheckPM;
  }

  public boolean selectFSGuard()
  {return state == State.StandBy;}

  @Action
  public void selectFS() {
    state = State.CheckFS;
  }

  public boolean selectDFMGuard()
  {return state == State.StandBy;}

  @Action
  public void selectDFM() {
    state = State.CheckDFM;
  }

  public boolean changedCSGuard()
  {return state == State.CheckCS;}

  @Action
  public void changedCS() {
    state = State.StandBy;
  }

  public boolean notChangedCSGuard()
  {return state == State.CheckCS;}

  @Action
  public void notChangedCS() {
    state = State.StandBy;
  }

  public boolean changedCSSGuard()
  {return state == State.CheckCSS;}

  @Action
  public void changedCSS() {
    state = State.StandBy;
  }

  public boolean notChangedCSSGuard()
  {return state == State.CheckCSS;}

  @Action
  public void notChangedCSS() {
    state = State.StandBy;
  }

  public boolean changedKMGuard()
  {return state == State.CheckKM;}

  @Action
  public void changedKM() {
    state = State.StandBy;
  }

  public boolean notChangedKMGuard()
  {return state == State.CheckKM;}

  @Action
  public void notChangedKM() {
    state = State.StandBy;
  }

  public boolean changedLAFGuard()
  {return state == State.CheckLAF;}

  @Action
  public void changedLAF() {
    state = State.StandBy;
  }

  public boolean notChangedLAFGuard()
  {return state == State.CheckLAF;}

  @Action
  public void notChangedLAF() {
    state = State.StandBy;
  }

  public boolean changedPMGuard()
  {return state == State.CheckPM;}

  @Action
  public void changedPM() {
    state = State.StandBy;
  }

  public boolean notChangedPMGuard()
  {return state == State.CheckPM;}

  @Action
  public void notChangedPM() {
    state = State.StandBy;
  }

  public boolean changedDFMGuard()
  {return state == State.CheckDFM;}

  @Action
  public void changedDFM() {
    state = State.StandBy;
  }

  public boolean notChangedDFMGuard()
  {return state == State.CheckDFM;}

  @Action
  public void notChangedDFM() {
    state = State.StandBy;
  }

  public boolean changedFSGuard()
  {return state == State.CheckFS;}

  @Action
  public void changedFS() {
    state = State.StandBy;
  }

  public boolean notChangedFSGuard()
  {return state == State.CheckFS;}

  @Action
  public void notChangedFS() {
    state = State.StandBy;
  }

  public void cleanup() throws Exception {
    qsAdapter.cleanup();
  }
}
