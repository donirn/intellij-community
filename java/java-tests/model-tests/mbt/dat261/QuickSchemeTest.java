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

import junit.framework.TestCase;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;

import javax.swing.*;

public class QuickSchemeTest extends TestCase{

  public void testQuickScheme() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      QuickSchemeModel qsModel = null;
      try {
        qsModel = new QuickSchemeModel();

        Tester tester = new GreedyTester(qsModel);
        tester.buildGraph();
        tester.addListener(new VerboseListener());
        tester.addListener(new StopOnFailureListener());
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new TransitionCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(2000);
        tester.printCoverage();

      }
      catch (Exception e) {
        e.printStackTrace();
        fail();
      }
      finally {
        try {
          if (qsModel != null) {
            qsModel.cleanup();
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
