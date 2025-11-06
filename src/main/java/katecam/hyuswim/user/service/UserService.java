package katecam.hyuswim.user.service;

import katecam.hyuswim.user.dto.UserSummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katecam.hyuswim.common.error.CustomException;
import katecam.hyuswim.common.error.ErrorCode;
import katecam.hyuswim.user.domain.User;
import katecam.hyuswim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

    @Transactional
    public void deleteUser(User loginUser, String confirmText) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
      if (!confirmText.equals("계정 탈퇴")){
          throw new CustomException(ErrorCode.WITHDRAWAL_FAILED);
      }
      user.delete();
    }

    @Transactional(readOnly = true)
    public UserSummaryResponse getNameAndHandle(User currentUser) {
        return new UserSummaryResponse(currentUser.getNickname(), currentUser.getHandle());
    }

}
