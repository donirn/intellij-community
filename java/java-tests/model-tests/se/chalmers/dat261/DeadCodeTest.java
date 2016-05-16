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
package se.chalmers.dat261;

import junit.framework.TestCase;
import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import org.junit.Test;
import se.chalmers.dat261.model.DeadCodeModel;

import javax.swing.*;

public class DeadCodeTest extends TestCase {
  public void testDeadCode() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      DeadCodeModel fireModel = null;
      try {
        fireModel = new DeadCodeModel();
        Tester tester = new RandomTester(fireModel);

        tester.buildGraph();
        tester.addListener(new VerboseListener());
        tester.addListener(new StopOnFailureListener());
        tester.addCoverageMetric(new TransitionCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());

        tester.generate(20);
        tester.printCoverage();
      }
      catch (Exception e) {
        e.printStackTrace();
        fail();
      }
      finally {
        try {
          fireModel.cleanup();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}
