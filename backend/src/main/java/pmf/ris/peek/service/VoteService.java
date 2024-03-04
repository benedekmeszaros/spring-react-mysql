package pmf.ris.peek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.ris.peek.model.Post;
import pmf.ris.peek.model.User;
import pmf.ris.peek.model.Vote;
import pmf.ris.peek.repository.VoteRepository;

@Service
public class VoteService {
	
	@Autowired
	private VoteRepository voteRepository;
	
	public Vote findVoteByPostAndUser(User user, Post post) {
		List<Vote> votes = voteRepository.findByUserAndPost(user.getId(), post.getId());
		if(votes.isEmpty())
			return null;
		else {
			return votes.get(0);
		}
	}
	
	public int saveVote(Vote vote) {
		try {
			return voteRepository.save(vote).getId();
		} catch (Exception e) {
			return -1;
		}
	}
}
