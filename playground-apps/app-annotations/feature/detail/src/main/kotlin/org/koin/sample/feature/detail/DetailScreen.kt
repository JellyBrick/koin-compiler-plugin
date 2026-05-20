package org.koin.sample.feature.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    newsId: String,
    viewModel: DetailViewModel = koinViewModel(key = newsId) { parametersOf(newsId) },
) {
    val resource by viewModel.newsResource.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        if (resource == null) {
            CircularProgressIndicator()
        } else {
            Text(
                text = resource!!.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Text(
                text = resource!!.content,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
