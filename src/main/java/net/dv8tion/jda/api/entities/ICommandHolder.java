package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Marker for entities that can have slash commands.
 *
 * @since 4.3
 *
 * @see Guild
 * @see JDA
 */
public interface ICommandHolder
{
    /**
     * Retrieves the list of guild commands.
     * <br>For {@link Guild} this list does not include global commands! Use {@link JDA#retrieveCommands()} for global commands.
     * <br>For {@link JDA} this list does not include guild commands! Use {@link Guild#retrieveCommands()} for guild commands.
     *
     * @return {@link RestAction} - Type: {@link List} of {@link Command}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<List<Command>> retrieveCommands();

    /**
     * Retrieves the existing {@link Command} instance by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param  id
     *         The command id
     *
     * @throws IllegalArgumentException
     *         If the provided id is not a valid snowflake
     *
     * @return {@link RestAction} - Type: {@link Command}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Command> retrieveCommandById(@Nonnull String id);

    /**
     * Retrieves the existing {@link Command} instance by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param  id
     *         The command id
     *
     * @return {@link RestAction} - Type: {@link Command}
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<Command> retrieveCommandById(long id)
    {
        return retrieveCommandById(Long.toUnsignedString(id));
    }

    /**
     * Creates or updates a command.
     * <br>If a command with the same name exists, it will be replaced.
     *
     * <p>To specify a complete list of all commands you can use {@link #updateCommands()} instead.
     *
     * <p>You need the OAuth2 scope {@code "applications.commands"} in order to add commands to a guild.
     *
     * <p><b>If using {@link JDA#upsertCommand(CommandData)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * @param  command
     *         The {@link CommandData} for the command
     *
     * @throws IllegalArgumentException
     *         If null is provided
     *
     * @return {@link CommandCreateAction}
     */
    @Nonnull
    @CheckReturnValue
    CommandCreateAction upsertCommand(@Nonnull CommandData command);

    /**
     * Creates or updates a command.
     * <br>If a command with the same name exists, it will be replaced.
     *
     * <p>To specify a complete list of all commands you can use {@link #updateCommands()} instead.
     *
     * <p>You need the OAuth2 scope {@code "applications.commands"} in order to add commands to a guild.
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * @param  name
     *         The lowercase alphanumeric (with dash) name, 1-32 characters
     * @param  description
     *         The description for the command, 1-100 characters
     *
     * @throws IllegalArgumentException
     *         If null is provided or the name/description do not meet the requirements
     *
     * @return {@link CommandCreateAction}
     */
    @Nonnull
    @CheckReturnValue
    default CommandCreateAction upsertCommand(@Nonnull String name, @Nonnull String description)
    {
        return upsertCommand(new CommandData(name, description));
    }

    /**
     * Configures the complete list of guild commands.
     * <br>This will replace the existing command list for this guild. You should only use this once on startup!
     *
     * <p>You need the OAuth2 scope {@code "applications.commands"} in order to add commands to a guild.
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * <h2>Examples</h2>
     * <pre>{@code
     * // Set list to 2 commands
     * <ICommandHolder>.updateCommands()
     *   .addCommands(new CommandData("ping", "Gives the current ping"))
     *   .addCommands(new CommandData("ban", "Ban the target user")
     *     .addOption(OptionType.USER, "user", "The user to ban", true))
     *   .queue();
     * // Delete all commands
     * guild.updateCommands().queue();
     * }</pre>
     *
     * @return {@link CommandListUpdateAction}
     */
    @Nonnull
    @CheckReturnValue
    CommandListUpdateAction updateCommands();

    /**
     * Edit an existing command by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to
     * propagate to the clients.</b>
     *
     * @param  id
     *         The id of the command to edit
     *
     * @throws IllegalArgumentException
     *         If the provided id is not a valid snowflake
     *
     * @return {@link CommandEditAction} used to edit the command
     */
    @Nonnull
    @CheckReturnValue
    CommandEditAction editCommandById(@Nonnull String id);

    /**
     * Edit an existing command by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * @param  id
     *         The id of the command to edit
     *
     * @return {@link CommandEditAction} used to edit the command
     */
    @Nonnull
    @CheckReturnValue
    default CommandEditAction editCommandById(long id)
    {
        return editCommandById(Long.toUnsignedString(id));
    }

    /**
     * Delete the command for this id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * @param  commandId
     *         The id of the command that should be deleted
     *
     * @throws IllegalArgumentException
     *         If the provided id is not a valid snowflake
     *
     * @return {@link RestAction}
     */
    @Nonnull
    @CheckReturnValue
    RestAction<Void> deleteCommandById(@Nonnull String commandId);

    /**
     * Delete the command for this id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link net.dv8tion.jda.api.requests.ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * <p><b>If using {@link JDA#upsertCommand(String, String)}, global commands can take up to <u>1 hour</u> to propagate to the clients.</b>
     *
     * @param  commandId
     *         The id of the command that should be deleted
     *
     * @return {@link RestAction}
     */
    @Nonnull
    @CheckReturnValue
    default RestAction<Void> deleteCommandById(long commandId)
    {
        return deleteCommandById(Long.toUnsignedString(commandId));
    }
}
