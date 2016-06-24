package uk.co.domcampbell.shoppinglist.dagger;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.view.HomeFragment;

/**
 * Created by Dominic on 23/06/16.
 */
@ActivityScope
@Component(dependencies = {ApplicationComponent.class}, modules = {HomePresenterModule.class})
public interface HomePresenterComponent {
    void inject(HomeFragment homeFragment);
}