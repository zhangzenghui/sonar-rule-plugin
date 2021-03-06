/*
 * Copyright 2018-2020 JD.com, Inc. QA Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.sonar.java.itqa.plugin.checks.naming;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.java.model.ModifiersUtils;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Modifier;

import java.util.regex.Pattern;

/**
 * @author: zhangliwei29
 *  @date: 2019/1/28
 */

@Rule(key = "p3c003")
public class AbstractClassShouldStartWithAbstractOrBaseRule extends BaseTreeVisitor implements JavaFileScanner{

    private static final String DEFAULT_FORMAT = "^(Abstract|Base)[A-Z][a-zA-Z0-9]*$";

    @RuleProperty(
            key = "format",
            description = "抽象类命名使用 Abstract 或 Base 开头",
            defaultValue = "" + DEFAULT_FORMAT)
    public String format = DEFAULT_FORMAT;

    private Pattern pattern = null;
    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        if (pattern == null) {
            pattern = Pattern.compile(format, Pattern.DOTALL);
        }
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitClass(ClassTree tree) {
        if (isAbstract(tree) && !hasCorrectNaming(tree)) {
            context.reportIssue(this, tree.simpleName(), "抽象类命名使用 Abstract 或 Base 开头");
        }
        super.visitClass(tree);
    }

    private static Boolean isAbstract(ClassTree tree) {
        return ModifiersUtils.hasModifier(tree.modifiers(), Modifier.ABSTRACT);
    }

    private  Boolean hasCorrectNaming(ClassTree tree) {
        if (tree.simpleName() == null) {
            return true;
        }
        return pattern.matcher(tree.simpleName().name()).matches();
    }
}