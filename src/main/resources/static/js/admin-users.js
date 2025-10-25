function filterUsers(status) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    rows.forEach(row => {
        if (status === 'all' || row.getAttribute('data-status') === status) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function searchUsers(keyword) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    const lowerKeyword = keyword.toLowerCase();
    rows.forEach(row => {
        const email = row.getAttribute('data-email').toLowerCase();
        const nickname = row.getAttribute('data-nickname').toLowerCase();
        if (email.includes(lowerKeyword) || nickname.includes(lowerKeyword)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function sortUsers(type) {
    // 정렬 기능은 서버사이드에서 처리하는 것을 권장
    console.log('정렬:', type);
}

function viewUser(userId) {
    // 상세 페이지로 이동
    window.location.href = `/admin/users/${userId}`;
}

function suspendUser(userId) {
    if (confirm('이 회원을 정지하시겠습니까?')) {
        console.log('정지:', userId);
        // TODO: 실제 API 호출
    }
}

function activateUser(userId) {
    if (confirm('이 회원을 활성화하시겠습니까?')) {
        console.log('활성화:', userId);
        // TODO: 실제 API 호출
    }
}
