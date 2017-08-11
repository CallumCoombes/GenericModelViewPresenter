package uk.demo.cal.genericmodelviewpresenter.GenericMvp;

/**
 * Gateway to the domain layer/business logic in MVP architecture.
 * Created by Callum on 20/02/2017.
 */
public abstract class GenericMvpModel implements GenericMvpContract.ModelOps{

    //Presenter Reference
    protected GenericMvpContract.RequiredPresenterOps mPresenter;

    //Required default constructor
    public GenericMvpModel(){}

    /**
     * Sets the presenter. Generally called after a generic instantiation of a Model in the
     * presenter.
     * @param mPresenter
     */
    public void setPresenter(GenericMvpContract.RequiredPresenterOps mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void onDestroy() {}
}
