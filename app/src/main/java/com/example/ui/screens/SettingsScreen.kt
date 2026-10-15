package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.User
import com.example.ui.theme.IgPrimary
import com.example.ui.theme.IgRedHeart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentUser: User,
    availableAccounts: List<User>,
    onBack: () -> Unit,
    onOpenSwitchAccount: () -> Unit,
    onOpenAddAccount: () -> Unit,
    onSwitchDirectly: (String) -> Unit,
    onOpenDeleteDeactivate: () -> Unit = {},
    onReactivateAccount: () -> Unit = {},
    onOpenAccountStatus: () -> Unit = {},
    onOpenSubscriptions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var searchQuery by remember { mutableStateOf("") }
    var isPrivateAccount by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings and activity",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.testTag("settings_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Settings
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search settings",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Current Active Account Header Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = currentUser.avatarRes),
                            contentDescription = "Current account avatar",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(Active)",
                                    color = IgPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = "@${currentUser.username}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onOpenSwitchAccount,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IgPrimary.copy(alpha = 0.15f),
                                contentColor = IgPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp).testTag("quick_switch_account_btn")
                        ) {
                            Text(text = "Switch", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Section: Account Switching & Other Accounts
            item {
                SettingsSectionHeader(title = "Account Management")
            }

            item {
                SettingsItemRow(
                    icon = Icons.Default.SwitchAccount,
                    title = "Switch Account",
                    subtitle = "Switch between ${availableAccounts.size} accounts",
                    onClick = onOpenSwitchAccount,
                    tag = "setting_switch_account"
                )
            }

            // Quick list of Other Accounts
            val otherAccounts = availableAccounts.filter { it.id != currentUser.id }
            if (otherAccounts.isNotEmpty()) {
                item {
                    Text(
                        text = "Other Accounts",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(start = 56.dp, top = 4.dp, bottom = 4.dp)
                    )
                }

                items(otherAccounts) { account ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSwitchDirectly(account.id) }
                            .padding(start = 56.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = account.avatarRes),
                            contentDescription = account.username,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "@${account.username}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp
                            )
                            Text(
                                text = account.fullName,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Log In",
                            color = IgPrimary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                SettingsItemRow(
                    icon = Icons.Default.PersonAdd,
                    title = "Add Account",
                    subtitle = "Log into or create another account",
                    tint = IgPrimary,
                    onClick = onOpenAddAccount,
                    tag = "setting_add_account"
                )
            }

            // Section: How you use Instagram
            item {
                SettingsSectionHeader(title = "How you use Instagram")
            }

            item {
                SettingsItemRow(
                    icon = Icons.Default.Subscriptions,
                    title = "Subscriptions",
                    subtitle = "Manage creator channel subscriptions",
                    onClick = onOpenSubscriptions,
                    tag = "setting_subscriptions_row"
                )
            }
            item {
                SettingsItemRow(
                    icon = Icons.Outlined.BookmarkBorder,
                    title = "Saved & Playlists",
                    subtitle = "Your curated posts, reels, and stories"
                )
            }
            item {
                SettingsItemRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    subtitle = "Pause all, likes, comments, messages"
                )
            }
            item {
                SettingsItemRow(
                    icon = Icons.Outlined.Timer,
                    title = "Time spent",
                    subtitle = "Daily limit, break reminders"
                )
            }

            // Section: Who can see your content
            item {
                SettingsSectionHeader(title = "Who can see your content")
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (isPrivateAccount) Icons.Default.Lock else Icons.Default.Public,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Private account",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = if (isPrivateAccount) "Only approved followers can see your posts" else "Anyone can see your photos and videos",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                    Switch(
                        checked = isPrivateAccount,
                        onCheckedChange = { isPrivateAccount = it }
                    )
                }
            }

            item {
                SettingsItemRow(
                    icon = Icons.Outlined.Group,
                    title = "Close Friends",
                    subtitle = "Manage your close friends list"
                )
            }

            // Section: Account Ownership & Control (Delete / Deactivate / Reactivate)
            item {
                SettingsSectionHeader(title = "Account Ownership and Control")
            }

            item {
                SettingsItemRow(
                    icon = Icons.Default.Gavel,
                    title = "Account Status",
                    subtitle = if (currentUser.isChannelTerminated) "Channel Terminated • 3 to 3 Copyright Strike • Content Removed" else "Account in good standing",
                    tint = if (currentUser.isChannelTerminated) MaterialTheme.colorScheme.error else IgPrimary,
                    onClick = onOpenAccountStatus,
                    tag = "setting_account_status_row"
                )
            }

            if (currentUser.isDeactivated || currentUser.isScheduledForDeletion) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentUser.isScheduledForDeletion) "Account Scheduled for Deletion" else "Account Deactivated",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${currentUser.deletionDaysLeft} days left until permanent deletion. Date: ${currentUser.deletionDate}. Reactivate now to restore your profile and messages.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onReactivateAccount,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IgPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reactivate_account_btn")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reactivate Account", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                item {
                    SettingsItemRow(
                        icon = Icons.Default.DeleteForever,
                        title = "Deactivation or Deletion",
                        subtitle = "Temporarily deactivate or permanently delete your account",
                        tint = MaterialTheme.colorScheme.error,
                        onClick = onOpenDeleteDeactivate,
                        tag = "setting_delete_deactivate_row"
                    )
                }
            }

            // Section: Login
            item {
                SettingsSectionHeader(title = "Logins")
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSwitchAccount() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Log out",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Log out @${currentUser.username}",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
    tag: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = tint
                )
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchAccountSheet(
    currentUserId: String,
    availableAccounts: List<User>,
    onSelectAccount: (String) -> Unit,
    onAddNewAccount: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("switch_account_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Switch Accounts",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                items(availableAccounts, key = { it.id }) { user ->
                    val isSelected = user.id == currentUserId

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectAccount(user.id)
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = user.username,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp
                            )
                            Text(
                                text = "@${user.username}",
                                fontSize = 12.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active account",
                                tint = IgPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), CircleShape)
                            )
                        }
                    }
                }

                // Add Account Option at bottom
                item {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onAddNewAccount)
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Account",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = "Add Instagram Account",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountSheet(
    onAddAccount: (username: String, fullName: String, bio: String, avatarRes: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedAvatarRes by remember { mutableStateOf(R.drawable.img_user_avatar) }

    val presetAvatars = listOf(
        R.drawable.img_user_avatar,
        R.drawable.img_post_travel,
        R.drawable.img_post_coffee,
        R.drawable.img_post_city
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("add_account_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Account",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Button(
                    onClick = {
                        if (username.isNotBlank() && fullName.isNotBlank()) {
                            onAddAccount(username, fullName, bio, selectedAvatarRes)
                        }
                    },
                    enabled = username.isNotBlank() && fullName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IgPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("submit_add_account_btn")
                ) {
                    Text("Log In", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Select Profile Picture
            Text(
                text = "Select Profile Photo",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(presetAvatars) { avatar ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable { selectedAvatarRes = avatar }
                            .then(
                                if (avatar == selectedAvatarRes) {
                                    Modifier.border(2.5.dp, IgPrimary, CircleShape)
                                } else Modifier
                            )
                    ) {
                        Image(
                            painter = painterResource(id = avatar),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                placeholder = { Text("e.g. creative_lens") },
                prefix = { Text("@") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_account_username_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Jordan Smith") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_account_fullname_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio (Optional)") },
                placeholder = { Text("Photographer • Designer • Wanderer") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DeleteDeactivateDialog(
    currentUser: User,
    onConfirm: (isPermanentDeletion: Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf("delete") } // "delete" or "deactivate"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Deactivating or Deleting your Instagram Account",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "If you want to take a break from Instagram, you can deactivate @${currentUser.username}. If you want to permanently delete your account, let us know.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Option: Deactivate
                Surface(
                    onClick = { selectedOption = "deactivate" },
                    shape = RoundedCornerShape(10.dp),
                    color = if (selectedOption == "deactivate") MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selectedOption == "deactivate") IgPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == "deactivate",
                            onClick = { selectedOption = "deactivate" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Deactivate account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Deactivating your account is temporary. Your profile, photos, comments and likes will be hidden until you reactivate.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option: Delete
                Surface(
                    onClick = { selectedOption = "delete" },
                    shape = RoundedCornerShape(10.dp),
                    color = if (selectedOption == "delete") MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f) else Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selectedOption == "delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == "delete",
                            onClick = { selectedOption = "delete" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Delete account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Deleting your account is permanent. When you delete your account, your profile, photos, reels, and comments will be permanently removed after 51 days.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedOption == "delete")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == "delete") MaterialTheme.colorScheme.error else IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("confirm_delete_deactivate_btn")
            ) {
                Text(if (selectedOption == "delete") "Delete Account" else "Deactivate Account")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier.testTag("delete_deactivate_dialog")
    )
}

@Composable
fun DeletionStatusDialog(
    daysLeft: Int = 51,
    deletionDate: String = "December 1, 2026",
    onReactivate: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Account Scheduled for Deletion",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$daysLeft days left",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: $deletionDate",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your account is currently hidden and scheduled for permanent deletion. You have $daysLeft days left until $deletionDate to reactivate your account and keep all your posts, reels, stories and messages.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onReactivate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("dialog_reactivate_account_btn")
            ) {
                Text("Reactivate Account")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        modifier = modifier.testTag("deletion_status_dialog")
    )
}

@Composable
fun ErrorDialog(
    title: String = "Something went wrong",
    message: String = "Please try again later",
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("error_dialog_ok_btn")
            ) {
                Text("OK")
            }
        },
        modifier = modifier.testTag("something_went_wrong_dialog")
    )
}

@Composable
fun TryAgainErrorDialog(
    title: String = "Something went wrong",
    message: String = "Please try again later",
    onTryAgain: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onTryAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("try_again_dialog_btn")
            ) {
                Text("Try again")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("try_again_dialog_cancel_btn")
            ) {
                Text("Cancel")
            }
        },
        modifier = modifier.testTag("something_went_wrong_try_again_dialog")
    )
}

@Composable
fun ActionNotAllowedDialog(
    title: String = "This action isn't allowed",
    message: String = "This action is restricted on this account. Please retry.",
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Block,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("retry_action_dialog_btn")
            ) {
                Text("RETRY")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier.testTag("action_not_allowed_dialog")
    )
}

@Composable
fun SessionExpiredDialog(
    title: String = "Something went wrong",
    message: String = "Session expired. Please log in.",
    onOk: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onOk,
        icon = {
            Icon(
                imageVector = Icons.Default.LockClock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onOk,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("session_expired_ok_btn")
            ) {
                Text("OK")
            }
        },
        modifier = modifier.testTag("session_expired_dialog")
    )
}

