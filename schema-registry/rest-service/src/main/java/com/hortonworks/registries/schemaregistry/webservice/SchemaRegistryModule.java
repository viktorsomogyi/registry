/**
 * Copyright 2016 Hortonworks.
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
 **/
package com.hortonworks.registries.schemaregistry.webservice;

import com.hortonworks.registries.common.ModuleRegistration;
import com.hortonworks.registries.common.util.FileStorage;
import com.hortonworks.registries.schemaregistry.DefaultSchemaRegistry;
import com.hortonworks.registries.schemaregistry.ISchemaRegistry;
import com.hortonworks.registries.storage.StorageManager;
import com.hortonworks.registries.storage.StorageManagerAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SchemaRegistryModule implements ModuleRegistration, StorageManagerAware {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaRegistryModule.class);

    private Map<String, Object> config;
    private FileStorage fileStorage;
    private StorageManager storageManager;

    @Override
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void init(Map<String, Object> config, FileStorage fileStorage) {
        this.config = config;
        this.fileStorage = fileStorage;
    }

    @Override
    public List<Object> getResources() {
        Collection<Map<String, Object>> schemaProviders = (Collection<Map<String, Object>>) config.get(ISchemaRegistry.SCHEMA_PROVIDERS);
        DefaultSchemaRegistry schemaRegistry = new DefaultSchemaRegistry(storageManager, fileStorage, schemaProviders);
        schemaRegistry.init(config);
        SchemaRegistryResource schemaRegistryResource = new SchemaRegistryResource(schemaRegistry);
        return Collections.<Object>singletonList(schemaRegistryResource);
    }

}
