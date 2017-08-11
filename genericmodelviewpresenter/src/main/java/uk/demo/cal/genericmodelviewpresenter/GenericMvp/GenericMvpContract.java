package uk.demo.cal.genericmodelviewpresenter.GenericMvp;

/**
 * Generic MVP contract containing all boilerplate methods as well as get methods to retrieve
 * specific implementation casts of generic objects.
 * Created by Callum on 20/02/2017.
 */
public class GenericMvpContract {

    /**
     * View mandatory methods. Available to Presenter
     *   <br>   Presenter -> View
     */
    public interface RequiredViewOps <PresenterType> {
        void showToast(String msg);
        void showAlert(String msg);
        /**
         * Get method that casts a generic Presenter to a specific Presenter. Returns the specific
         * Presenter type.
         * Example:
         * <pre>
         * {@code
         * public SpecificPresenterClass getSpecificImpOfGenericPresenter() {
         *                          return (SpecificPresenterClass) this.mPresenter;
         *                          }
         * }
         * </pre>
         */
        PresenterType getSpecificImpOfGenericPresenter();
        // any other ops
    }

    /**
     * Operations offered from Presenter to View
     *   <br>   View -> Presenter
     */
    public interface PresenterOps <ModelType, FragmentType>{
        void onDestroy(boolean isChangingConfig);
        void onConfigurationChanged(GenericMvpContract.RequiredViewOps view);
        /**
         * Get method that casts a generic Model to a specific Model. Returns the specific
         * Model type.
         * Example:
         * <pre>
         * {@code
         *  public SpecificModelClass getSpecificImpOfGenericModel() {
         *                       return (SpecificModelClass) this.mModel;
         *                       }
         * }
         * </pre>
         */
        ModelType getSpecificImpOfGenericModel();
        /**
         * Get method that casts a generic Fragment to a specific Fragment. Returns the specific
         * Fragment type.
         * Example:
         * <pre>
         * {@code
         *  public SpecificFragmentClass getSpecificImpOfGenericView() {
         *                         return (SpecificFragmentClass)this.mView.get();
         *                         }
         * }
         * </pre>
         */
        FragmentType getSpecificImpOfGenericView();
        // any other ops to be called from View
    }

    /**
     * Operations offered from Presenter to Model
     *   <br>   Model -> Presenter
     */
    public interface RequiredPresenterOps {
        void onError(String errorMsg);
        // Any other returning operation Model -> Presenter
    }

    /**
     * Model operations offered to Presenter
     *   <br>   Presenter -> Model
     */
    public interface ModelOps <PresenterType>{
        void onDestroy();
        void setPresenter(GenericMvpContract.RequiredPresenterOps mPresenter);
        /**
         * Get method that casts a generic Presenter to a specific Presenter. Returns the specific
         * Presenter type.
         * Example:
         * <pre>
         * {@code
         * public SpecificPresenterClass getSpecificImpOfGenericPresenter() {
         *                          return (SpecificPresenterClass) this.mPresenter;
         *                          }
         * }
         * </pre>
         */
        PresenterType getSpecificImpOfGenericPresenter();
        // Any other data operation
    }

}
