function filterPosts(status) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    rows.forEach(row => {
        if (status === 'all' || row.getAttribute('data-status') === status) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function searchPosts(keyword) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    const lowerKeyword = keyword.toLowerCase();
    rows.forEach(row => {
        const title = row.getAttribute('data-title').toLowerCase();
        const writer = row.getAttribute('data-writer').toLowerCase();
        if (title.includes(lowerKeyword) || writer.includes(lowerKeyword)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function sortPosts(type) {
    // 서버 사이드 정렬 권장
    console.log('정렬:', type);
}
