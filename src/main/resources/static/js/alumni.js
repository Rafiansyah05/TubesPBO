// JavaScript khusus untuk halaman Alumni

document.addEventListener('DOMContentLoaded', function () {
  yId('jobForm');
  if (jobForm) {
    jobForm.addEventListener('submit', function (e) {
      const perusahaan = document.getElementById('companyId');
      const jabatan = document.getElementById('jabatan');
      const startDate = document.getElementById('startDate');

      if (!perusahaan.value || !jabatan.value || !startDate.value) {
        e.preventDefault();
        showNotification('Harap isi semua field yang wajib!', 'error');
      }
    });
  }

  const deleteButtons = document.querySelectorAll('.delete-job');
  deleteButtons.forEach((button) => {
    button.addEventListener('click', function (e) {
      if (!confirmDelete('Hapus riwayat pekerjaan ini?')) {
        e.preventDefault();
      }
    });
  });

  const profileForm = document.getElementById('profileForm');
  if (profileForm) {
    profileForm.addEventListener('submit', function (e) {
      const name = document.getElementById('name');
      const email = document.getElementById('email');

      if (!name.value || !email.value) {
        e.preventDefault();
        showNotification('Nama dan Email tidak boleh kosong!', 'error');
      }
    });
  }

  const previewBtn = document.getElementById('previewBtn');
  if (previewBtn) {
    previewBtn.addEventListener('click', function () {
      const name = document.getElementById('name')?.value || '-';
      const email = document.getElementById('email')?.value || '-';
      const major = document.getElementById('major')?.value || '-';

      alert(`Preview Data:\n\nNama: ${name}\nEmail: ${email}\nJurusan: ${major}`);
    });
  }

  const majorFilter = document.getElementById('majorFilter');
  if (majorFilter) {
    majorFilter.addEventListener('change', function () {
      const selectedMajor = this.value;
      if (selectedMajor) {
        window.location.href = `/career/statistic/major?major=${selectedMajor}`;
      }
    });
  }

  const companySearch = document.getElementById('companySearch');
  if (companySearch) {
    companySearch.addEventListener('input', function () {
      const keyword = this.value.toLowerCase();
      const companyItems = document.querySelectorAll('.company-item');

      companyItems.forEach((item) => {
        const companyName = item.querySelector('.company-name')?.textContent.toLowerCase();
        if (companyName && companyName.includes(keyword)) {
          item.style.display = 'flex';
        } else {
          item.style.display = 'none';
        }
      });
    });
  }

  function updateJobCount() {
    const jobCards = document.querySelectorAll('.job-card');
    const jobCount = document.getElementById('jobCount');
    if (jobCount) {
      jobCount.textContent = jobCards.length;
    }
  }

  updateJobCount();
});
