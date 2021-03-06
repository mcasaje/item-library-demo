package controllers.items;

import models.items.Item;
import models.items.utils.sorting.items.ItemFieldSortStrategy;
import models.items.utils.sorting.items.ItemSortStrategy;

import java.util.List;

/**
 * Provides methods to retrieve and sort instances of {@link models.items.Item}.
 */
public interface ItemsController {

    Item createItem(String name, int itemTypeId, String username);

    Item getItem(int itemId, String username);

    List<Item> getItems(int itemTypeId, String username, ItemSortStrategy sortStrategy);

    List<Item> getItems(int itemTypeId, String username, ItemSortStrategy sortStrategy, int tagId);

    List<Item> sortItemByField(List<Item> items, int fieldId, ItemFieldSortStrategy sortStrategy);

    void deleteItem(int itemId, String username);

}
