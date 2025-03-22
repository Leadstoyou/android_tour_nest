package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.Order;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.util.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderService<T> {
    private static final GenericRepository<Order> orderRepository = new GenericRepository<>("orders", Order.class);
    private static final GenericRepository<Tour> tourRepository = new GenericRepository<>("tours", Tour.class);
    private FirebaseCallback<T> callback;

    public OrderService() {
    }

    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }

    public static GenericRepository<Order> getRef() {
        return orderRepository;
    }

    public static OrderService<Boolean> create(Order order) {
        OrderService<Boolean> service = new OrderService<>();
        LogUtil.logMessage("order0 :: " + order);

        if (order == null || order.getTourId() == null || order.getUserId() == null) {
            if (service.callback != null) {
                service.callback.onFailure(new Exception("Order, Tour ID, or User ID is null"));
            }
            return service;
        }
        LogUtil.logMessage("order0 :: " + order);
        tourRepository.get(order.getTourId(), new FirebaseCallback<Tour>() {
            @Override
            public void onSuccess(Tour tour) {
                try {
                    if (tour == null) {
                        if (service.callback != null) {
                            service.callback.onFailure(new Exception("Tour not found"));
                        }
                        return;
                    }
                    LogUtil.logMessage("tour :: " + tour);
                    int totalPassengers = order.getAdultCount() + order.getChildCount();
                    int availableSlots = tour.getSlot();

                    if (availableSlots >= totalPassengers) {
                        tour.setSlot(availableSlots - totalPassengers);
                        tourRepository.update(order.getTourId(), tour);

                        if (order.getOrderId() == null) {
                            order.setOrderId(UUID.randomUUID().toString());
                        }
                        if (order.getOrderDate() == null) {
                            order.setOrderDate(new Date());
                        }
                        LogUtil.logMessage("order :: " + order);
                        orderRepository.create(order);
                        LogUtil.logMessage("order2 :: " + order);
                        if (service.callback != null) {
                            service.callback.onSuccess(true);
                        }
                    } else {
                        if (service.callback != null) {
                            service.callback.onFailure(new Exception("Not enough slots available"));
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

    public static OrderService<Order> getById(String orderId) {
        OrderService<Order> service = new OrderService<>();
        orderRepository.get(orderId, new FirebaseCallback<Order>() {
            @Override
            public void onSuccess(Order result) {
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

    public static OrderService<List<Order>> getByUserId(String userId) {
        OrderService<List<Order>> service = new OrderService<>();
        orderRepository.getAll(new FirebaseCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                List<Order> userOrders = new ArrayList<>();
                if (result != null) {
                    for (Order order : result) {
                        if (order.getUserId() != null && order.getUserId().equals(userId)) {
                            userOrders.add(order);
                        }
                    }
                }
                if (service.callback != null) {
                    service.callback.onSuccess(userOrders);
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

    public static void updateStatus(String orderId, String newStatus, FirebaseCallback<Boolean> callback) {
        orderRepository.get(orderId, new FirebaseCallback<Order>() {
            @Override
            public void onSuccess(Order order) {
                if (order != null) {
                    order.setStatus(newStatus);
                    orderRepository.update(orderId, order);
                    callback.onSuccess(true);
                } else {
                    callback.onFailure(new Exception("Order not found"));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }
}