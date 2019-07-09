/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zotikos.m4u.ui.popularmovies.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.ItemMovieBinding
import com.zotikos.m4u.ui.common.DataBoundListAdapter
import com.zotikos.m4u.ui.popularmovies.dto.MovieUIDto
import com.zotikos.m4u.util.AppExecutors

class MovieListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((MovieUIDto, ImageView) -> Unit)?
) : DataBoundListAdapter<MovieUIDto, ItemMovieBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<MovieUIDto>() {
        override fun areItemsTheSame(oldItem: MovieUIDto, newItem: MovieUIDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieUIDto, newItem: MovieUIDto): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.imageUrl == newItem.imageUrl
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemMovieBinding {
        val binding = DataBindingUtil
            .inflate<ItemMovieBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_movie,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener {
            binding.movieItem?.let {
                callback?.invoke(it, binding.postImageView)
            }
        }


        return binding
    }

    override fun bind(binding: ItemMovieBinding, item: MovieUIDto, position: Int) {
        binding.movieItem = item
        binding.postImageView.transitionName =
            "%s_%d".format(binding.root.context.getString(R.string.hero_image_transition), position)
        binding.postTitle.transitionName =
            "%s_%d".format(binding.root.context.getString(R.string.title_transition), position)
    }
}
