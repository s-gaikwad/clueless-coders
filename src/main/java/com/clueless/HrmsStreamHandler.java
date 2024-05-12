/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.clueless;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;

import com.clueless.handlers.CancelandStopIntentHandler;
import com.clueless.handlers.CheckInIntentHandler;
import com.clueless.handlers.CheckOutIntentHandler;
import com.clueless.handlers.FallbackIntentHandler;
import com.clueless.handlers.HelpIntentHandler;
import com.clueless.handlers.LeaveIntentHandler;
import com.clueless.handlers.ZohoIntentHandler;
import com.clueless.handlers.LaunchRequestHandler;
import com.clueless.handlers.SessionEndedRequestHandler;


public class HrmsStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                    new CancelandStopIntentHandler(),
                    new LaunchRequestHandler(),
                    new ZohoIntentHandler(),
                    new CheckInIntentHandler(),
                    new CheckOutIntentHandler(),
                    new LeaveIntentHandler(),
                    new HelpIntentHandler(),
                    new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.53114cdc-2a62-4232-9215-93a6421d9205")
                .build();
    }

    public HrmsStreamHandler() {
        super(getSkill());
    }

}
