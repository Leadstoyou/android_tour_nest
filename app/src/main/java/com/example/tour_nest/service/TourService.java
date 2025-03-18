package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.Tour;

import java.util.List;

public class TourService<T> {
    private static final GenericRepository<Tour> tourRepository = new GenericRepository<>("tours", Tour.class);
    private FirebaseCallback<T> callback;

    public TourService() {
    }

    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }

    public static void create(Tour tour) {
        TourService<Boolean> service = new TourService<>();

        try {
            tourRepository.create(tour);
            if (service.callback != null) service.callback.onSuccess(true);
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
    }

    public static TourService<List<Tour>> getAllTour() {
        TourService<List<Tour>> service = new TourService<>();
        try {
            tourRepository.getAll(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    if (service.callback != null) service.callback.onSuccess(result);
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) {
                        service.callback.onFailure(e);
                    }
                }
            });
        } catch (Exception e) {
            if (service.callback != null) {
                service.callback.onFailure(e);
            }
        }
        return service;
    }

    public static TourService<Tour> getById(String id) {
        TourService<Tour> service = new TourService<>();
        try {
            tourRepository.get(id, new FirebaseCallback<Tour>() {
                @Override
                public void onSuccess(Tour result) {
                    if (service.callback != null) service.callback.onSuccess(result);
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) {
                        service.callback.onFailure(e);
                    }
                }
            });
        } catch (Exception e) {
            if (service.callback != null) {
                service.callback.onFailure(e);
            }
        }
        return service;
    }

    public static TourService<List<Tour>> search(String field,String value) {
        TourService<List<Tour>> service = new TourService<>();
        try {
            tourRepository.search(field,value,new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    if (service.callback != null) service.callback.onSuccess(result);
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) {
                        service.callback.onFailure(e);
                    }
                }
            });
        } catch (Exception e) {
            if (service.callback != null) {
                service.callback.onFailure(e);
            }
        }
        return service;
    }
}
