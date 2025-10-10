function filterComments(status) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    rows.forEach(row => {
        if (status === 'all' || row.getAttribute('data-status') === status) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function searchComments(keyword) {
    const rows = document.querySelectorAll('tbody tr[data-status]');
    const lowerKeyword = keyword.toLowerCase();
    rows.forEach(row => {
        const writer = row.getAttribute('data-writer').toLowerCase();
        const content = row.getAttribute('data-content').toLowerCase();
        if (writer.includes(lowerKeyword) || content.includes(lowerKeyword)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function sortComments(type) {
    // 정렬 기능은 서버사이드에서 처리하는 것을 권장
    console.log('정렬:', type);
}
