/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mybatis.mapping;

import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mybatis.mapping.model.Column;

/**
 * Interface for a MyBatis-specific {@link PersistentProperty}.
 *
 * @author JARVIS SONG
 * @since 1.0.0
 */
public interface MybatisPersistentProperty extends PersistentProperty<MybatisPersistentProperty> {

	/**
	 * Return whether the property is considered embeddable.
	 * @return whether the property is embeddable
	 */
	boolean isEmbeddable();

	Column getColumn();

	boolean isEnumerated();

}
