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
package com.jd.sonar.java.itqa.plugin.checks.other;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.java.checks.helpers.ExpressionsHelper;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: yangshuo8
 * @date: 2019/3/18
 * @time: 下午16.02
 * @desc: AvoidMissUseOfMathRandomRule
 */
@Rule(key = "p3c050")
public class AvoidMissUseOfMathRandomRule extends IssuableSubscriptionVisitor {

    private static final String TYPE_REGEX = "(int|long)";
    private static final String RANDOM = "Math.random";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(Tree.Kind.TYPE_CAST);
    }

    @Override
    public void visitNode(Tree tree) {
        TypeCastTree typeCastTree = (TypeCastTree) tree;
        String type = typeCastTree.symbolType().name();
        if (type.matches(TYPE_REGEX)) {
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            typeCastTree.expression().accept(expressionVisitor);
            if (expressionVisitor.isIssue) {
                reportIssue(typeCastTree.expression(), "请直接使用Random对象的 'nextInt' 或者 'nextLong' 方法");
            }
        }
        super.visitNode(tree);
    }

    private static class ExpressionVisitor extends BaseTreeVisitor {

        private Boolean isIssue = false;

        @Override
        public void visitMemberSelectExpression(MemberSelectExpressionTree tree) {
            String expression = ExpressionsHelper.concatenate(tree);
            if (RANDOM.equals(expression)) {
                isIssue = true;
            }
        }
    }
}
