document.addEventListener('DOMContentLoaded', function () {
  const deleteAlumniButtons = document.querySelectorAll('.delete-alumni');
  deleteAlumniButtons.forEach((button) => {
    button.addEventListener('click', function (e) {
      const alumniName = this.getAttribute('data-name') || 'Alumni';
      if (!confirmDelete(`Hapus data alumni ${alumniName}?`)) {
        e.preventDefault();
      }
    });
  });

  const verifyButtons = document.querySelectorAll('.verify-alumni');
  verifyButtons.forEach((button) => {
    button.addEventListener('click', function (e) {
      const alumniName = this.getAttribute('data-name') || 'Alumni';
      if (!confirm(`Verifikasi data alumni ${alumniName}?`)) {
        e.preventDefault();
      }
    });
  });

  const searchInput = document.getElementById('searchAlumni');
  const searchForm = document.getElementById('searchForm');

  if (searchInput) {
    let debounceTimer;
    searchInput.addEventListener('input', function () {
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(() => {
        const keyword = this.value;
        if (keyword.length >= 2 || keyword.length === 0) {
          performSearch(keyword);
        }
      }, 500);
    });
  }

  function performSearch(keyword) {
    if (searchForm) {
      const searchInputField = document.getElementById('searchKeyword');
      if (searchInputField) {
        searchInputField.value = keyword;
      }
      searchForm.submit();
    }
  }

  const addAlumniForm = document.getElementById('addAlumniForm');
  if (addAlumniForm) {
    addAlumniForm.addEventListener('submit', function (e) {
      const idUser = document.getElementById('idUser');
      const name = document.getElementById('name');
      const email = document.getElementById('email');
      const major = document.getElementById('major');

      if (!idUser.value || !name.value || !email.value || !major.value) {
        e.preventDefault();
        showNotification('Harap isi semua field!', 'error');
      }

      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (email.value && !emailPattern.test(email.value)) {
        e.preventDefault();
        showNotification('Format email tidak valid!', 'error');
      }
    });
  }

  const selectAllCheckbox = document.getElementById('selectAll');
  if (selectAllCheckbox) {
    selectAllCheckbox.addEventListener('change', function () {
      const checkboxes = document.querySelectorAll('.alumni-checkbox');
      checkboxes.forEach((cb) => {
        cb.checked = this.checked;
      });
      updateBulkActionButton();
    });
  }

  function updateBulkActionButton() {
    const checkboxes = document.querySelectorAll('.alumni-checkbox:checked');
    const bulkDeleteBtn = document.getElementById('bulkDeleteBtn');
    if (bulkDeleteBtn) {
      bulkDeleteBtn.disabled = checkboxes.length === 0;
      bulkDeleteBtn.textContent = `Hapus (${checkboxes.length})`;
    }
  }

  const alumniCheckboxes = document.querySelectorAll('.alumni-checkbox');
  alumniCheckboxes.forEach((cb) => {
    cb.addEventListener('change', updateBulkActionButton);
  });

  const bulkDeleteBtn = document.getElementById('bulkDeleteBtn');
  if (bulkDeleteBtn) {
    bulkDeleteBtn.addEventListener('click', function (e) {
      const checkedCount = document.querySelectorAll('.alumni-checkbox:checked').length;
      if (checkedCount === 0) {
        e.preventDefault();
        showNotification('Tidak ada alumni yang dipilih!', 'error');
      } else if (!confirmDelete(`Hapus ${checkedCount} data alumni yang dipilih?`)) {
        e.preventDefault();
      }
    });
  }

  const exportBtn = document.getElementById('exportData');
  if (exportBtn) {
    exportBtn.addEventListener('click', function () {
      showNotification('Mengekspor data...', 'success');
      window.location.href = '/admin/export/alumni';
    });
  }

  function refreshStats() {
    fetch('/admin/api/stats')
      .then((response) => response.json())
      .then((data) => {
        const totalAlumni = document.getElementById('totalAlumni');
        const totalJobs = document.getElementById('totalJobs');
        const totalCompanies = document.getElementById('totalCompanies');

        if (totalAlumni) totalAlumni.textContent = data.totalAlumni || 0;
        if (totalJobs) totalJobs.textContent = data.totalJobs || 0;
        if (totalCompanies) totalCompanies.textContent = data.totalCompanies || 0;
      })
      .catch((error) => console.log('Error refreshing stats:', error));
  }
});
