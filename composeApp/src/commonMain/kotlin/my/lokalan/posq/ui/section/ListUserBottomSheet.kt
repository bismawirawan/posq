package my.lokalan.posq.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.lokalan.posq.presentation.transaction.addtransaction.UserItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import my.lokalan.posq.presentation.user.model.UserUIData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUserSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    scope: CoroutineScope,
    data: List<UserUIData>,
    onBottomSheetChange: (Boolean) -> Unit,
    onSelectUser: (UserUIData) -> Unit
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun closeSheet() {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onBottomSheetChange(false)
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier.fillMaxSize(),
        onDismissRequest = { onBottomSheetChange(false) },
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            itemsIndexed(
                items = data,
                key = { index, item -> index }
            ) { index, item ->
                UserItem(item) {
                    closeSheet()
                    onSelectUser(item)
                }
            }
        }
    }
}
