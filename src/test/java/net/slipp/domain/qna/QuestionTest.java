package net.slipp.domain.qna;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import net.slipp.domain.tag.Tag;
import net.slipp.domain.user.SocialUser;
import net.slipp.repository.tag.TagRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTest {
    private Question dut;

    @Mock
    private TagRepository tagRepository;

    @Before
    public void setup() {
        dut = new Question();
    }

    @Test
    public void isWritedBy_sameUser() throws Exception {
        // given
        SocialUser user = new SocialUser(10);
        dut = new Question(user, null, null, null);

        // when
        boolean actual = dut.isWritedBy(user);

        // then
        assertThat(actual, is(true));
    }

    @Test
    public void isWritedBy_differentUser() throws Exception {
        // given
        SocialUser user = new SocialUser(10);
        dut = new Question(user, null, null, null);

        // when
        boolean actual = dut.isWritedBy(new SocialUser(11));

        // then
        assertThat(actual, is(false));
    }

    @Test
    public void newQuestion() throws Exception {
        // given
        SocialUser loginUser = new SocialUser();
        Tag java = new Tag("java");
        QuestionDto dto = new QuestionDto("title", "contents", "java javascript");
        when(tagRepository.findByName(java.getName())).thenReturn(java);

        // when
        Question newQuestion = new Question(loginUser, dto.getTitle(), dto.getContents(), Sets.newHashSet(java));

        // then
        assertThat(newQuestion.getTitle(), is(dto.getTitle()));
        assertThat(newQuestion.getContents(), is(dto.getContents()));
        assertThat(newQuestion.hasTag(java), is(true));
        assertThat(newQuestion.getDenormalizedTags(), is(java.getName()));
    }

    @Test
    public void tag() throws Exception {
        Tag tag = new Tag("newTag");
        Question question = new Question();
        question.tag(tag);

        Set<Tag> tags = Sets.newHashSet(tag);
        assertThat(question.getTags(), is(tags));
        assertThat(tag.getTaggedCount(), is(1));
    }

    @Test
    public void contents() throws Exception {
        String contents = "this is contents";
        dut.setContents(contents);
        assertThat(dut.getContents(), is(contents));
    }

    @Test
    public void getBestAnswer() throws Exception {
        dut = new Question() {
            @Override
            public List<Answer> getAnswers() {
                return Lists.newArrayList(createAnswerWithSumLike(1), createAnswerWithSumLike(0),
                        createAnswerWithSumLike(3));
            }
        };

        Answer bestAnswer = dut.getBestAnswer();
        assertThat(bestAnswer.getSumLike(), is(3));
    }

    @Test
    public void getBestAnswerDontExisted() throws Exception {
        dut = new Question() {
            @Override
            public List<Answer> getAnswers() {
                return Lists.newArrayList(createAnswerWithSumLike(1), createAnswerWithSumLike(0));
            }
        };

        assertThat(dut.getBestAnswer(), is(nullValue()));
    }

    @Test
    public void getBestAnswerHasNotAnswer() throws Exception {
        assertThat(dut.getBestAnswer(), is(nullValue()));
    }

    private Answer createAnswerWithSumLike(Integer sumLike) {
        final Answer answer = new Answer();
        answer.setSumLike(sumLike);
        return answer;
    }
}
