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
package se.chalmers.dat261.model;

import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;
import se.chalmers.dat261.adapter.DeadCodeAdapter;

public class DeadCodeModel implements FsmModel {
  private State state = State.NoDeadCode;
  private DeadCodeAdapter adapter;

  public DeadCodeModel() throws Exception {
    adapter = new DeadCodeAdapter();
  }

  private enum State {DeadCode, NoDeadCode};

  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {

  }

  @Action
  public void addDeadCode() {
    adapter.addDeadCode();
  }

  public void cleanup() throws Exception {
    adapter.cleanup();
  }
}
