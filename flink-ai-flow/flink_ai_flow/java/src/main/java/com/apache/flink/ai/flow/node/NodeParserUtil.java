/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.apache.flink.ai.flow.node;

import com.alibaba.fastjson.JSONObject;
import com.apache.flink.ai.flow.common.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class NodeParserUtil {

	private static Map<String, String> parserMap = new HashMap<>();
	static {
		NodeParserUtil.registerNodeParser("Example", ExampleNode.class);
		NodeParserUtil.registerNodeParser("Transformer", TransformerNode.class);
		NodeParserUtil.registerNodeParser("Predictor", PredictorNode.class);
		NodeParserUtil.registerNodeParser("Trainer", TrainerNode.class);
	}
	public static void registerNodeParser(String name, Class c){
		parserMap.put(name, c.getCanonicalName());
	}

	public static BaseNode parseNode(JSONObject jsonObject) throws Exception{
		String type = jsonObject.getString("__class__");
		try {
			NodeParser parser = ReflectUtil.createInstance(parserMap.get(type));
			return parser.parseFromJSONObject(jsonObject);
		} catch (IllegalAccessException | InvocationTargetException
				| InstantiationException | NoSuchMethodException | ClassNotFoundException e) {
			throw e;
		}
	}
}
