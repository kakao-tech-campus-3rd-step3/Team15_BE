function filterReports(status) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    rows.forEach(row => {
        if (status === 'all' || row.getAttribute('data-status') === status) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function searchReports(keyword) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    const lowerKeyword = keyword.toLowerCase();
    rows.forEach(row => {
        const reporter = row.getAttribute('data-reporter').toLowerCase();
        const post = row.getAttribute('data-post').toLowerCase();
        if (reporter.includes(lowerKeyword) || post.includes(lowerKeyword)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function sortReports(type) {
    // 실제 정렬은 서버사이드에서 처리하는 것을 권장
    console.log('정렬:', type);
}
