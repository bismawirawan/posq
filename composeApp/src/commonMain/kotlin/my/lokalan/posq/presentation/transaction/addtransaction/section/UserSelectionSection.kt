package my.lokalan.posq.presentation.transaction.addtransaction.section

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import my.posq.shared.Background
import my.posq.shared.Sage
import my.lokalan.posq.presentation.user.model.UserUIData
@Composable
fun UserSelectionSection(
    userList: List<UserUIData>, onUserSelected: (UserUIData) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers = userList.filter {
        it.fullname.contains(searchQuery, ignoreCase = true) || it.username.contains(
            searchQuery, ignoreCase = true
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari Anggota") },
            modifier = Modifier.fillMaxWidth().border(1.dp, Sage, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredUsers) { user ->
                UserItem(user = user, onClick = { onUserSelected(user) })
            }
        }
    }
}
