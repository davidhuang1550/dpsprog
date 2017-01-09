package com.example.david.dpsproject.Fragments.CreatePost;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Presenter.CreatePostPresenter;

/**
 * Created by david on 2017-01-04.
 */

interface CreatePostInterface{
    Post checkvalidation();
    CreatePostPresenter createpost(Post post);
}
