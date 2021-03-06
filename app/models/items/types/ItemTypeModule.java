/*
 * Created by IntelliJ IDEA.
 * User: mcasaje
 * Date: 9/5/2015
 * Time: 9:13 PM
 */
package models.items.types;

import com.google.inject.AbstractModule;

public class ItemTypeModule extends AbstractModule {
    protected void configure() {
        bind(ItemTypeFactory.class).to(ItemTypeFactoryImpl.class);
        bind(ItemTypeRepository.class).to(ItemTypeRepositoryImpl.class);
    }
}
