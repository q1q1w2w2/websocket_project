package com.example.websocket.user.repository;

import com.example.websocket.user.domain.Friend;
import com.example.websocket.user.domain.FriendShip;
import com.example.websocket.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("select fs from FriendShip fs where fs.user = :user order by fs.createdAt desc")
    List<FriendShip> findAllByUser(@Param("user") User user);

    Optional<FriendShip> findByUserAndFriend(User user, User friend);
}
