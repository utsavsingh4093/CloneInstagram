package com.java.main.service;

import com.java.main.entity.AddFollowers;
import com.java.main.dto.AddFollowersWrapper;
import com.java.main.entity.User;
import com.java.main.dto.UserWrapper;
import com.java.main.repository.AddFollowRepo;
import com.java.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AddFollowService {

    @Autowired
    private AddFollowRepo addFollowRepo;

    @Autowired
    UserRepository userRepository;

    public AddFollowers getByUserAndFollowerId(int userId, int followerId) {
        return addFollowRepo.findByUser_IdAndFollowedId(Integer.valueOf(userId), followerId);
    }

    @Transactional
    public String followers(int userId, int followedId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AddFollowers> existingFollowers = addFollowRepo.findByUser_IdAndFollowedId(userId, followedId);

        if (!existingFollowers.isEmpty()) {
            addFollowRepo.delete(existingFollowers.get(0));
            return "Follow removed successfully.";
        } else {
            AddFollowers addFollowers = new AddFollowers();
            addFollowers.setFollowedId(followedId);
            addFollowers.setUser(user);
            addFollowers.setType(AddFollowers.FollowType.FOLLOWING);
            addFollowRepo.save(addFollowers);
            return "Follow added successfully.";
        }
    }

    @Transactional
    public void followBack(int userId, int followedId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AddFollowers> existingFollowers = addFollowRepo.findByUser_IdAndFollowedId(userId, followedId);

        if (existingFollowers.isEmpty()) {
            System.out.println("Empty USer");
        } else {
            AddFollowers followEntry = existingFollowers.get(0);
            if (followEntry.getType() == AddFollowers.FollowType.FOLLOWING) {
                followEntry.setType(AddFollowers.FollowType.UNFOLLOW);
                addFollowRepo.save(followEntry);
            } else {
                addFollowRepo.delete(followEntry);
            }
        }
    }


    public List<AddFollowersWrapper> getAllFollowingByUserIdAndFollowId(int userId, int followId) {
        List<AddFollowersWrapper> list = new ArrayList<>();
        List<AddFollowers> addFollowersList = addFollowRepo.findByUser_IdAndFollowedId(userId, followId);

        for (AddFollowers addFollowers : addFollowersList) {
            addFollowers.setFollowType(addFollowers.getType().toString());
            AddFollowersWrapper wrapper = new AddFollowersWrapper(
                    addFollowers.getFollowId(),
                    addFollowers.getUser().getId(),
                    addFollowers.getFollowedId(),
                    addFollowers.getType().toString()
            );
            list.add(wrapper);

        }
        return list;
    }

    //This method is use to get compelete follow data

    public List<AddFollowersWrapper> getAllFollowersByUserId(int userId) {
        List<AddFollowersWrapper> list = new ArrayList<>();
        for (AddFollowers user : addFollowRepo.findByFollowedId(userId)) {
            list.add(new AddFollowersWrapper(user.getFollowId(), user.getUser().getId(), user.getFollowedId(), user.getType().toString()));
        }
        return list;
    }

    public List<UserWrapper> getAllFollowersByUserIds(int userId) {
        System.out.println("This is My User Id Mean My Follow ID : " + userId);
        List<UserWrapper> listUser = new ArrayList<>();
        UserWrapper userWrapper = null;
        //Int above 3 line i change to display image at all side
        for (AddFollowers followers : addFollowRepo.findByFollowedId(userId)) {
            userWrapper = new UserWrapper();
            User user = userRepository.findById(followers.getUser().getId()).get();
            userWrapper.setId(user.getId());
            userWrapper.setStringImageFile("data:image/png;base64," + Base64.getEncoder().encodeToString(user.getImage_data()));
            userWrapper.setFirst_name(user.getFirst_name());
            userWrapper.setLast_name(user.getLast_name());
            userWrapper.setCity(user.getCity());
            userWrapper.setFollowType(followers.getType().toString());
            listUser.add(userWrapper);
        }
        return listUser;
    }

}