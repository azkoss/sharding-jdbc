/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.core.optimizer;

import io.shardingjdbc.core.optimizer.insert.InsertOptimizeEngine;
import io.shardingjdbc.core.optimizer.query.QueryOptimizeEngine;
import io.shardingjdbc.core.parsing.parser.sql.SQLStatement;
import io.shardingjdbc.core.parsing.parser.sql.dml.DMLStatement;
import io.shardingjdbc.core.parsing.parser.sql.dml.insert.InsertStatement;
import io.shardingjdbc.core.parsing.parser.sql.dql.select.SelectStatement;
import io.shardingjdbc.core.routing.router.sharding.GeneratedKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Optimize engine factory.
 *
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptimizeEngineFactory {
    
    /**
     * Create optimize engine instance.
     * 
     * @param sqlStatement SQL statement
     * @param parameters parameters
     * @param generatedKey generated key
     * @return optimize engine instance
     */
    public static OptimizeEngine newInstance(final SQLStatement sqlStatement, final List<Object> parameters, final GeneratedKey generatedKey) {
        if (sqlStatement instanceof InsertStatement) {
            return new InsertOptimizeEngine(sqlStatement.getConditions().getOrCondition(), parameters, generatedKey); 
        }
        if (sqlStatement instanceof SelectStatement || sqlStatement instanceof DMLStatement) {
            return new QueryOptimizeEngine(sqlStatement.getConditions().getOrCondition(), parameters);
        }
        // TODO do with DDL and DAL
        return new QueryOptimizeEngine(sqlStatement.getConditions().getOrCondition(), parameters);
    }
}
