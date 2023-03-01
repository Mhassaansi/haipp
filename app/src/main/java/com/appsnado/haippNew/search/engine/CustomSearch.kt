package com.appsnado.haippNew.search.engine

import com.appsnado.haippNew.R

/**
 * A custom search engine.
 */
class CustomSearch(queryUrl: String) : BaseSearchEngine(
    "file:///android_asset/lightning.png",
    queryUrl,
    R.string.search_engine_custom
)
