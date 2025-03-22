package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FavouriteService<T> {
    private static final GenericRepository<Favourite> favouriteRepository = new GenericRepository<>("favourites", Favourite.class); // Sửa "categories" thành "favourites"
    private FirebaseCallback<T> callback;

    public FavouriteService() {
    }
    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }
    public static GenericRepository<Favourite> getRef(){
        return favouriteRepository;
    }

    public static FavouriteService<Boolean> create(String tourId, String userId) {
        FavouriteService<Boolean> service = new FavouriteService<>();

        if (tourId == null || userId == null) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("Tour ID or User ID is null"));
            }
            return service;
        }

        favouriteRepository.get(userId, new FirebaseCallback<Favourite>() {
            @Override
            public void onSuccess(Favourite result) {
                try {
                    if (result != null) {
                        List<String> favoriteTours = result.getFavouriteTours();
                        if (favoriteTours == null) {
                            favoriteTours = new ArrayList<>();
                            result.setFavouriteTours(favoriteTours);
                        }
                        boolean isFavorited = favoriteTours.contains(tourId);

                        if (isFavorited) {
                            favoriteTours.remove(tourId);
                        } else {
                            favoriteTours.add(tourId);
                        }
                        favouriteRepository.update(userId, result);

                        if (service.callback != null) {
                            service.callback.onSuccess(!isFavorited);
                        }
                    } else {
                        List<String> initialList = new ArrayList<>();
                        initialList.add(tourId);
                        Favourite newFavourite = new Favourite(userId, initialList);
                        favouriteRepository.createWithKey(userId, newFavourite);

                        if (service.callback != null) {
                            service.callback.onSuccess(true);
                        }
                    }
                } catch (Exception e) {
                    if (service.callback != null) {
                        service.callback.onFailure(e);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) {
                    service.callback.onFailure(e);
                }
            }
        });

        return service;
    }

    public static void getFavouriteByIdRealtime(String userId, FirebaseCallback<Favourite> callback) {
        favouriteRepository.getRealtimeById(userId, callback);
    }
    public static FavouriteService<Favourite> getByUserId(String userId){
        FavouriteService<Favourite> service = new FavouriteService<>();
        favouriteRepository.get(userId, new FirebaseCallback<Favourite>() {
            @Override
            public void onSuccess(Favourite result) {
                if (service.callback != null) {
                    service.callback.onSuccess(result);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (service.callback != null) {
                    service.callback.onFailure(e);
                }
            }
        });

        return service;
    }
}