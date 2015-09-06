package models.items;

import models.items.fields.Field;
import models.items.itemfields.ItemField;
import models.items.itemfields.ItemFieldRepository;
import models.items.tags.Tag;
import models.items.tags.TagRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Queries for {@link Item} instances.
 * <p>
 * Acting facade for querying {@link Tag}, {@link Field} instances.
 */
class ItemRepositoryImpl implements ItemRepository {

    private final ItemFactory itemFactory;
    private final TagRepository tagRepository;
    private ItemFieldRepository itemFieldRepository;

    @Inject
    ItemRepositoryImpl(ItemFactory itemFactory, TagRepository tagRepository, ItemFieldRepository itemFieldRepository) {
        this.itemFactory = itemFactory;
        this.tagRepository = tagRepository;
        this.itemFieldRepository = itemFieldRepository;
    }

    @Override
    public Item findItem(EntityManager entityManager, int id) {

        final String queryString = "SELECT o FROM ItemDAO o WHERE o.id=:ID";
        TypedQuery<ItemDAO> query = entityManager.createQuery(queryString, ItemDAO.class);
        query.setParameter("ID", id);
        ItemDAO dao = query.getSingleResult();
        id = dao.getId();
        String name = dao.getName();
        String usernameOfOwner = dao.getUsernameOfOwner();

        Set<Tag> tags = tagRepository.findTags(entityManager, id);
        List<ItemField> itemFields = itemFieldRepository.findItemFields(entityManager, id);

        return itemFactory.createItem(id, name, usernameOfOwner, tags, itemFields);

    }

    @Override
    public Item findItem(EntityManager entityManager, String name, String usernameOfOwner) {

        final String queryString = "SELECT o FROM ItemDAO o WHERE o.name=:NAME AND o.usernameOfOwner=:USERNAME_OF_OWNER";
        TypedQuery<ItemDAO> query = entityManager.createQuery(queryString, ItemDAO.class);
        query.setParameter("NAME", name);
        query.setParameter("USERNAME_OF_OWNER", usernameOfOwner);
        ItemDAO dao = query.getSingleResult();
        int id = dao.getId();
        name = dao.getName();

        assert usernameOfOwner.equals(dao.getUsernameOfOwner());

        Set<Tag> tags = tagRepository.findTags(entityManager, id);
        List<ItemField> itemFields = itemFieldRepository.findItemFields(entityManager, id);

        return itemFactory.createItem(id, name, usernameOfOwner, tags, itemFields);

    }

    @Override
    public List<Item> findItems(EntityManager entityManager, String usernameOfOwner) {

        final String queryString = "SELECT o FROM ItemDAO o WHERE o.usernameOfOwner=:USERNAME_OF_OWNER";
        TypedQuery<ItemDAO> query = entityManager.createQuery(queryString, ItemDAO.class);
        query.setParameter("USERNAME_OF_OWNER", usernameOfOwner);
        List<ItemDAO> daoList = query.getResultList();

        ArrayList<Item> items = new ArrayList<>();

        for (ItemDAO dao : daoList) {
            int id = dao.getId();
            String name = dao.getName();

            assert usernameOfOwner.equals(dao.getUsernameOfOwner());

            Set<Tag> tags = tagRepository.findTags(entityManager, id);
            List<ItemField> itemFields = itemFieldRepository.findItemFields(entityManager, id);
            Item item = itemFactory.createItem(id, name, usernameOfOwner, tags, itemFields);
            items.add(item);
        }

        items.trimToSize();

        return items;

    }

    @Override
    public Item insertItem(EntityManager entityManager, Item item) {

        ItemDAO dao = this.createDAO(item);

        entityManager.persist(dao);

        int id = dao.getId();
        String name = dao.getName();
        String usernameOfOwner = dao.getUsernameOfOwner();

        return itemFactory.createItem(id, name, usernameOfOwner);

    }

    @Override
    public void deleteItem(EntityManager entityManager, Item item) {

        ItemDAO dao = this.createDAO(item);

        entityManager.merge(dao);
        entityManager.remove(dao);

    }

    private ItemDAO createDAO(Item item) {

        int id = item.getId();
        String name = item.getName();
        String username = item.getUsername();

        ItemDAO dao = new ItemDAO();
        dao.setId(id);
        dao.setName(name);
        dao.setUsernameOfOwner(username);

        return dao;
    }
}
