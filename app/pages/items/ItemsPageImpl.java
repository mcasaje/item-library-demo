package pages.items;

import controllers.appusers.sessions.SessionAuthController;
import controllers.appusers.sessions.UnauthorizedException;
import controllers.items.ItemsController;
import models.items.Item;
import models.items.utils.sorting.ItemSortStrategy;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;
import views.html.pages.items.index;

import javax.inject.Inject;
import java.util.List;

class ItemsPageImpl extends Controller implements ItemsPage {

    private final String PAGE_TITLE = "Items";
    private final String ITEM_NAME_ID = "new_item_name";
    private SessionAuthController sessionAuthController;
    private ItemsController itemsController;

    @Inject
    ItemsPageImpl(SessionAuthController sessionAuthController, ItemsController itemsController) {
        this.sessionAuthController = sessionAuthController;
        this.itemsController = itemsController;
    }

    @Override
    public Result get(String username) {

        try {
            sessionAuthController.isSessionValid(session());

            List<Item> items = itemsController.getItems(username, ItemSortStrategy.ID_DESC);
            return ok((Content) index.render(username + "'s " + PAGE_TITLE, null, items, ITEM_NAME_ID));

        } catch (UnauthorizedException e) {
            return redirect(pages.appusers.routes.LoginPage.get());
        }

    }

    @Override
    public Result post(String username) {

        try {
            sessionAuthController.isSessionValid(session());

            DynamicForm form = Form.form().bindFromRequest();
            String itemName = form.get(ITEM_NAME_ID);


            return redirect(pages.items.routes.ItemsPage.get(username));

        } catch (UnauthorizedException e) {
            return redirect(pages.appusers.routes.LoginPage.get());
        }

    }
}
