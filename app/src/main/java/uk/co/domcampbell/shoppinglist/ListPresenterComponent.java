package uk.co.domcampbell.shoppinglist;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.view.ListFragment;

/**
 * Created by Dominic on 23/06/16.
 */
@ActivityScope
@Component(dependencies = {MyComponent.class}, modules = {ListPresenterModule.class})
public interface ListPresenterComponent {
    void inject(ListFragment listFragment);
}
