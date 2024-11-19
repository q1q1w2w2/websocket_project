// header
function toFriendList() {
    window.location.href = '/friend/list';
}

function getToChatList() {
    window.location.href = `/chat/list?accessToken=${accessToken}`;
}

function logout() {
    fetch("/api/logout", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + accessToken
        },
        body: JSON.stringify({refreshToken: refreshToken})
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 시 토큰 삭제 및 로그인 페이지로 이동
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('accessToken');
                window.location.href = '/login';
                alert("로그아웃 되었습니다.");
            } else {
                throw new Error('로그아웃 실패');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('로그아웃 중 오류가 발생했습니다');
            window.location.href = '/login';
        });
}
