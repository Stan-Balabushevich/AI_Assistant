package id.slava.nt.chatgpthelper.presentation.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Checkbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange // Pass the callback to update state externally
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isChecked) "Android tutor activated" else "Android tutor deactivated",
            fontSize = 14.sp
        )
    }
}
