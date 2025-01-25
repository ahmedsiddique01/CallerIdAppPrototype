package com.caller.id.app.prototype.viewmodels

import com.caller.id.app.prototype.CoroutineTestRule
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.blocked.BlockedContactsUseCase
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import com.caller.id.app.prototype.presentation.contacts.viewmodel.ContactsViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactsViewModelTest {

    private lateinit var viewModel: ContactsViewModel
    private val contactsUseCase = mockk<ContactsUseCase>()
    private val blockedContactsUseCase = mockk<BlockedContactsUseCase>()
    private val contact = Contact(
        id = "5",
        name = "Johnson",
        number = "111111"
    )

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        viewModel = ContactsViewModel(contactsUseCase, blockedContactsUseCase)
    }

    @Test
    fun `fetch contacts successfully`() = runTest {

        coEvery { contactsUseCase.searchContacts("") } returns flow {
            emit(listOf(contact, contact))
        }

        viewModel.fetchContacts()
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 2
    }

    @Test
    fun `fetch contacts with empty list`() = runTest {
        coEvery { contactsUseCase.searchContacts("") } returns flow {
            emit(emptyList())
        }

        viewModel.fetchContacts()
        advanceUntilIdle()

        viewModel.contacts.value?.size shouldBe 0
    }

    @Test
    fun `search contacts successfully`() = runTest {
        coEvery { contactsUseCase.searchContacts("John") } returns flow {
            emit(listOf(contact))
        }

        viewModel.searchContacts("John")
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 1
        result?.first()?.name shouldBe "Johnson"
    }

    @Test
    fun `search contacts with no results`() = runTest {
        coEvery { contactsUseCase.searchContacts("Unknown") } returns flow {
            emit(emptyList())
        }

        viewModel.searchContacts("Unknown")
        advanceUntilIdle()

        val result = viewModel.contacts.value
        result?.size shouldBe 0
    }

}
