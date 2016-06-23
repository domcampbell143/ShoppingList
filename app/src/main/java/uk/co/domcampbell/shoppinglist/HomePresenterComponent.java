package uk.co.domcampbell.shoppinglist;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.view.HomeFragment;

/**
 * Created by Dominic on 23/06/16.
 */
@ActivityScope
@Component(dependencies = {MyComponent.class}, modules = {HomePresenterModule.class})
public interface HomePresenterComponent {
    void inject(HomeFragment homeFragment);
}