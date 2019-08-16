package com.olgaz.testnytimesmostpopular.fragments;

import com.olgaz.testnytimesmostpopular.api.ApiClient;
import com.olgaz.testnytimesmostpopular.api.ApiConstants;
import com.olgaz.testnytimesmostpopular.api.ApiService;
import com.olgaz.testnytimesmostpopular.model.NewsResults;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class MostPopularPresenter {
    private MostPopularView view;
    private Disposable disposable;

    public MostPopularPresenter(MostPopularView view) {
        this.view = view;
    }

    void loadData(String tabArticle) {
        ApiClient apiClient = ApiClient.getInstance();
        ApiService apiService = apiClient.getApiService();
        disposable = apiService.getResponseNews(tabArticle, ApiConstants.PERIOD, ApiConstants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsResults>() {
                    @Override
                    public void accept(NewsResults newsResults) throws Exception {
                        view.showData(newsResults.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError(throwable.getMessage());
                    }
                });
    }

    void disposeDisposable() {
        if (disposable != null) disposable.dispose();
    }

}
