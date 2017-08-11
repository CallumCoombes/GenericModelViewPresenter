package uk.demo.cal.genericmodelviewpresenter.GenericMvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import butterknife.Unbinder;

/**
 * Created by Callum on 07/03/2017.
 */

public abstract class GenericMvpActivity<PresenterType extends GenericMvpPresenter, ModelType extends GenericMvpModel> extends AppCompatActivity implements GenericMvpContract.RequiredViewOps {

    protected final String TAG = getClass().getSimpleName();
    protected GenericMvpFragment.OnFragmentInteractionListener mListener;
    protected StateMaintainer mStateMaintainer;
    protected PresenterType mPresenter;
    protected Unbinder mUnbinder;
    protected boolean firstTimeIn;

    /**
     * Oncreate takes two generic parameters that define the specific class implementation of the
     * presenter and model. If the fragment is already present in the stateMaintainer it will be
     * reinitialised, else it will be initialised for the first time.
     * @param presenterType Specific class implementation of Presenter
     * @param modelType Specific class implementation of Model
     * @param savedInstanceState Standard onCreate saved instance state parameter
     */
    public void onCreate(Class<PresenterType> presenterType, Class<ModelType> modelType, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( mStateMaintainer == null)
            mStateMaintainer =
                    new StateMaintainer(this.getSupportFragmentManager(), TAG + "_retainer" );
        try {
            if ( mStateMaintainer.firstTimeIn() ) {
                Log.d(TAG, "First time calling onCreate()");
                initialise(presenterType, modelType,  this);
            } else {
                Log.d(TAG, "Second (or subsequent) time calling onCreate()");
                reinitialise(presenterType, modelType, this);
            }
        } catch ( Exception e ) {
            Log.d(TAG, "onCreate() " + e );
            throw new RuntimeException( e );
        }
    }

    public void initialiseView(){
        if(firstTimeIn){
            this.mPresenter.initialiseView();
        }else{
            mPresenter.reloadView();
        }
    }

    /**
     * Initialise the fragment, called if there is no instance of the fragment in the state
     * maintainer
     * @param presenterType Specific class implementation of Presenter
     * @param modelType Specific class implementation of Model
     * @param view RequiredViewOps view
     * @throws java.lang.InstantiationException
     * @throws IllegalAccessException
     */
    private void initialise(Class<PresenterType> presenterType, Class<ModelType> modelType, GenericMvpContract.RequiredViewOps view)
            throws java.lang.InstantiationException, IllegalAccessException{
        this.mPresenter = presenterType.newInstance();
        this.mPresenter.setView(modelType, view);
        mStateMaintainer.put(/*GenericMvpContract.PresenterOps.class.getSimpleName()*/TAG, mPresenter);
        this.firstTimeIn = true;
    }

    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     * @param presenterType Specific class implementation of Presenter
     * @param modelType Specific class implementation of Model
     * @param view RequiredViewOps view
     */
    private void reinitialise(Class<PresenterType> presenterType, Class<ModelType> modelType, GenericMvpContract.RequiredViewOps view)
            throws java.lang.InstantiationException, IllegalAccessException {
        mPresenter = mStateMaintainer.get(/*GenericMvpContract.PresenterOps.class.getClass().getSimpleName()*/TAG);
        if ( mPresenter == null ) {
            Log.w(TAG, "recreating Presenter");
            initialise(presenterType, modelType, view );
        } else {
            mPresenter.onConfigurationChanged( view );
            this.firstTimeIn = false;
        }
    }

    @Override
    public void onDestroy() {
        //Unbind Butterknife bindings
        if(mUnbinder!=null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    // Show AlertDialog
    @Override
    public void showAlert(String msg) {
        // show alert Box
    }

    // Show Toast
    @Override
    public void showToast(String msg) {
        if(this != null) {
            Toast.makeText(this,TAG + ": " + msg, Toast.LENGTH_SHORT).show();
        }
    }
}
