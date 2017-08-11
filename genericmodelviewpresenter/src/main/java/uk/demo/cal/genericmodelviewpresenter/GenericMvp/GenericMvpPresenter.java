package uk.demo.cal.genericmodelviewpresenter.GenericMvp;

import java.lang.ref.WeakReference;


/**
 * Created by Callum on 20/02/2017.
 */

public abstract class GenericMvpPresenter<ModelType extends GenericMvpModel> implements GenericMvpContract.PresenterOps, GenericMvpContract.RequiredPresenterOps {

    protected final String TAG = getClass().getSimpleName();
    //Layer View reference
    protected WeakReference<GenericMvpContract.RequiredViewOps> mView;
    //Layer Model reference
    protected GenericMvpContract.ModelOps mModel;
    //Configuration change state
    protected boolean mIsChangingConfig;

    //Required default presenter
    public GenericMvpPresenter() {
    }

    /**
     * Sets the view class. Generally called after a generic instantiation of a Presenter in the
     * Fragment.
     * @param modelTypeClass Specific implementation of a Model class
     * @param mView RequiredViewOps view
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void setView(Class<ModelType> modelTypeClass, GenericMvpContract.RequiredViewOps mView) throws IllegalAccessException, InstantiationException {
        this.mView = new WeakReference<>(mView);
        this.mModel = modelTypeClass.newInstance();
        this.mModel.setPresenter(this);
    }

    /**
     * Receives {@link GenericMvpFragment#onDestroy()} event
     * @param isChangingConfig  Config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {
        mView = null;
        mIsChangingConfig = isChangingConfig;
        if ( !isChangingConfig ) {
            mModel.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(GenericMvpContract.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void onError(String errorMsg) {

    }

    /**
     * Initialises view for first time run.
     * ONLY called when there is no instance of a presenter in existence.
     */
    public void initialiseView(){}

    /**
     * Reloads view for second (or subsequent) run.
     * ONLY called when an instance of the presenter already exists and the
     * view can be reloaded.
     */
    public void reloadView(){}

}
