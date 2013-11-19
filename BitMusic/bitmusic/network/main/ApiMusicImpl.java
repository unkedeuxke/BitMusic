/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bitmusic.network.main;

import bitmusic.network.api.ApiMusic;
import bitmusic.music.data.Comment;
import bitmusic.music.data.Song;
import bitmusic.network.exception.NetworkException;
import bitmusic.network.message.AbstractMessage;
import bitmusic.network.message.EnumTypeMessage;
import bitmusic.network.message.MessageAddComment;
import bitmusic.network.message.MessageGetSongsByUser;
import bitmusic.network.message.MessageSearchSongsByTag;
import bitmusic.network.message.MessageTagRequest;
import java.util.List;

/**
 *
 * @author florian
 */
public final class ApiMusicImpl implements ApiMusic {
    /**
    * Singleton implementation.
    */
    private static final ApiMusicImpl APIMUSICIMPL = new ApiMusicImpl();

    /**
     * Private constructor for singleton pattern.
     */
    private ApiMusicImpl() { }

    /**
     * .
     * @return Unique instance of ApiMusicImpl
     */
    protected static ApiMusicImpl getInstance() {
        return APIMUSICIMPL;
    }

    /*########################################################################*/
    /* IMPLEMENTED METHODS */
    /*########################################################################*/
    /**
     * Send a comment about a song to a distant user.
     *
     * @param song song concerned
     * @param comment the new comment to add
     * @throws NetworkException thrown when the user doesn't exist
    */
    @Override
    public void addComment(final Song song, final Comment comment)
            throws NetworkException {
        //Get the source address
        final String sourceAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        sourceAddress = Controller.getInstance().
                getUserIpFromDirectory(song.getOwnerId());

        final AbstractMessage message;

        message = new MessageAddComment(
                EnumTypeMessage.AddComment,
                sourceAddress,
                Controller.getBroadcastAddress(),
                song.getOwnerId(),
                song,
                comment);
        Controller.getInstance().getThreadManager().assignTaskToHermes(message);
    }
    /**
     * Ask a distant user to search for keywords in his tags.
     * This is an asynchrounous call
     *
     * @param operator user operating the research
     * @param askedUser user asked
     * @param idResearch the id of the research
     * @param keywordsList keywords searched
     * @param option 0 = ALL keywords must match (default), 1 = ANY
     * keyword match
     */
    @Override
    public void tagRequest(final String operator, final String askedUser,
            final String idResearch, final List<String> keywordsList,
            final int option) throws NetworkException {
        //Get the source address
        final String sourceAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        sourceAddress = Controller.getInstance().
                getUserIpFromDirectory(operator);

        final String destAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        destAddress = Controller.getInstance().
                getUserIpFromDirectory(askedUser);

        final AbstractMessage message;

        message = new MessageTagRequest(
                EnumTypeMessage.TagRequest,
                sourceAddress,
                destAddress,
                operator,
                askedUser,
                idResearch,
                keywordsList,
                option);
        Controller.getInstance().getThreadManager().assignTaskToHermes(message);
    }
    /**
     * Ask a distant user to search for keywords in his tags.
     * Implements the default option option = 0 (ALL keywords must match)
     * This is an asynchrounous call
     *
     * @param operator user operating the research
     * @param askedUser user asked
     * @param idResearch the id of the research
     * @param keywordsList keywords searched
     */
    @Override
    public void tagRequest(final String operator, final String askedUser,
            final String idResearch, final List<String> keywordsList)
            throws NetworkException {
        this.tagRequest(operator, askedUser, idResearch, keywordsList, 0);
    }
    /**
     * Network message send to a distant user to ask him to send his songList.
     *
     * @param askedUser user to ask
     * @param researchId id of the research
     */
    @Override
    public void getSongsByUser(final String operator, final String askedUser,
            final String researchId) throws NetworkException {
        //Get the source address
        final String sourceAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        sourceAddress = Controller.getInstance().
                getUserIpFromDirectory(operator);

        //Get the remote address
        final String destAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        destAddress = Controller.getInstance().
                getUserIpFromDirectory(askedUser);

        final AbstractMessage message;

        message = new MessageGetSongsByUser(
                EnumTypeMessage.GetSongsByUser,
                sourceAddress,
                destAddress,
                askedUser,
                operator,
                researchId);
        Controller.getInstance().getThreadManager().assignTaskToHermes(message);
    }
    /**
     * Search songs in the LAN  with tag specified in the search.
     *
     * @param operator asking user
     * @param userIdDest id of the distant user
     * @param searchId id of the research
     * @param tagList list of tags
     */
    @Override
    public void searchSongsByTags(final String operator,
            final String userIdDest, final String searchId,
            final List<String> tagList) throws NetworkException {
        //Get the source address
        final String sourceAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        sourceAddress = Controller.getInstance().
                getUserIpFromDirectory(operator);

        //Get the remote address
        final String destAddress;

        //Warning, it may emmit an exception thrown to the calling method!
        destAddress = Controller.getInstance().
                getUserIpFromDirectory(userIdDest);

        final AbstractMessage message;

        message = new MessageSearchSongsByTag(
                EnumTypeMessage.SearchSongsByTag,
                sourceAddress,
                destAddress,
                searchId,
                tagList,
                userIdDest);
        Controller.getInstance().getThreadManager().assignTaskToHermes(message);
    }
    /**
     *Return the list of users’ IDs.
     *
     * @return listUserId
     */
    @Override
    public List<String> getAllUserId() {
        return Controller.getInstance().getUserListFromDirectory();
    }
}
