package uk.demo.cal.genericmodelviewpresenter.GenericMvp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import butterknife.Unbinder;

/**
 * Generic implementation of a MVP architectured Fragment. A fragment may extend this class and
 * inherit the majority of the boilerplate code not only for Fragments, but also for MVP.
 * Created by Callum on 20/02/2017.
 */
public abstract class GenericMvpFragment<PresenterType extends GenericMvpPresenter, ModelType extends GenericMvpModel> extends android.support.v4.app.Fragment implements GenericMvpContract.RequiredViewOps{

    protected final String TAG = getClass().getSimpleName();
    protected GenericMvpFragment.OnFragmentInteractionListener mListener;
    protected StateMaintainer mStateMaintainer;
    protected PresenterType mPresenter;
    protected Unbinder mUnbinder;
    protected boolean firstTimeIn;

    // Required empty public constructor
    public GenericMvpFragment(){}

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
                    new StateMaintainer(getActivity().getSupportFragmentManager(), TAG + "_retainer" );
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
        String x = TAG;
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
        String x = TAG;
        mPresenter = mStateMaintainer.get(TAG/*GenericMvpContract.PresenterOps.class.getClass().getSimpleName()*/);
        if ( mPresenter == null ) {
            Log.w(TAG, "recreating Presenter");
            initialise(presenterType, modelType, view );
        } else {
            mPresenter.onConfigurationChanged( view );
            this.firstTimeIn = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GenericMvpFragment.OnFragmentInteractionListener) {
            mListener = (GenericMvpFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Unbind Butterknife bindings
        if(mUnbinder!=null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Show AlertDialog
    @Override
    public void showAlert(String msg) {
        // show alert Box
    }

    // Show Toast
    @Override
    public void showToast(String msg) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(),TAG + ": " + msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void onFragmentBackPressed(){};

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * @return Activity Context
     */
    public Context getActivityContext() {
        return getActivity();
    }

    /**
     * @return Application Context
     */
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }


}
