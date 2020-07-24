package com.jd.sonar.java.itqa.plugin.checks.set;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

/**
 * Created by weiqiao on 2020/3/25.
 */
public class SetShouldBeUsedToCallContainsRuleTest {

    @Test
    public void test(){
        JavaCheckVerifier.verify("src/test/files/set/SetShouldBeUsedToCallContainsCheck.java", new SetShouldBeUsedToCallContainsRule());
    }

}
