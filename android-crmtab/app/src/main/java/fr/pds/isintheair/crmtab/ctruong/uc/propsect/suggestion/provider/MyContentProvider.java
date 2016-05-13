package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Insert;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;

import fr.pds.isintheair.crmtab.jbide.uc.registercall.database.entity.CallEndedEvent;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.database.entity.CallEndedEvent_Table;
import fr.pds.isintheair.crmtab.model.dao.CacheDao;
import fr.pds.isintheair.crmtab.model.database.OrmTabDataBase;
import fr.pds.isintheair.crmtab.model.entity.Event;
import fr.pds.isintheair.crmtab.model.entity.Event_Table;
import fr.pds.isintheair.crmtab.model.mock.Contact;
import fr.pds.isintheair.crmtab.model.mock.Contact_Table;

/**
 * Content provider class
 *
 * @author Truong
 * @since 5/5/2016
 */
public class MyContentProvider extends ContentProvider {

    private OrmTabDataBase dataBase;
    private CacheDao mDatabase;
    // Define the authority of the provider
    private static final String AUTHORITY = "fr.pds.isintheair.crm.controller.ctruong.uc.propsect.suggestion.provider.MyContentProvider";


    // Define end point of uris
    private static final String PATH_MESSAGE = "message";
    private static final String PATH_CRV = "crv";
    private static final String PATH_CALL = "CallEndedEvent";
    private static final String PATH_CONTACT = "Contact";
    private static final String PATH_EVENT = "Event";

    // Define id for each uri
    private static final int MESSAGE = 1;
    private static final int CRV = 2;
    private static final int CALL = 3;
    private static final int CONTACT = 4;
    private static final int EVENT = 5;

    // Uri content
    public static final Uri CONTENT_URI_MESSAGE = Uri.parse("content://" + AUTHORITY + "/" + PATH_MESSAGE);
    public static final Uri CONTENT_URI_CRV = Uri.parse("content://" + AUTHORITY + "/" + PATH_CRV);
    public static final Uri CONTENT_URI_CALL = Uri.parse("content://" + AUTHORITY + "/" + PATH_CALL);
    public static final Uri CONTENT_URI_CONTACT = Uri.parse("content://" + AUTHORITY + "/" + PATH_CONTACT);
    public static final Uri CONTENT_URI_EVENT = Uri.parse("content://" + AUTHORITY + "/" + PATH_EVENT);

    // Content type
    public static final String CONTENT_TYPE_MESSAGE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_MESSAGE;
    public static final String CONTENT_TYPE_CRV = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_CRV;
    public static final String CONTENT_TYPE_CALL = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_CALL;
    public static final String CONTENT_TYPE_CONTACT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_CONTACT;
    public static final String CONTENT_TYPE_EVENT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_EVENT;

    // Uri matcher
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, PATH_MESSAGE, MESSAGE);
        sURIMatcher.addURI(AUTHORITY, PATH_CRV, CRV);
        sURIMatcher.addURI(AUTHORITY, PATH_CALL, CALL);
        sURIMatcher.addURI(AUTHORITY, PATH_CONTACT, CONTACT);
        sURIMatcher.addURI(AUTHORITY, PATH_EVENT, EVENT);
    }

    public MyContentProvider() {

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mDatabase.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        int count = 0;
        switch (match) {
            case MESSAGE:
                count = database.delete(CacheDao.TABLE_NAME, "id=" + uri.getLastPathSegment() + " and " + selection,
                        selectionArgs);
                break;
            case CRV:
                count = database.delete(CacheDao.TABLE_NAME_VISIT_REPORT, "id=" + uri.getLastPathSegment() + " and " + selection,
                        selectionArgs);
                break;
            case CALL:
                new Delete().from(CallEndedEvent.class).where(CallEndedEvent_Table.id.eq(Long.parseLong(uri.getLastPathSegment())));
                break;
            case CONTACT:
                new Delete().from(Contact.class).where(CallEndedEvent_Table.id.eq(Long.parseLong(uri.getLastPathSegment())));
                break;
            case EVENT:
                new Delete().from(Event.class).where(CallEndedEvent_Table.id.eq(Long.parseLong(uri.getLastPathSegment())));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return count;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     *
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case MESSAGE:
                return CONTENT_TYPE_MESSAGE;
            case CRV:
                return CONTENT_TYPE_CRV;
            case CALL:
                return CONTENT_TYPE_CALL;
            case CONTACT:
                return CONTENT_TYPE_CONTACT;
            case EVENT:
                return CONTENT_TYPE_EVENT;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
        long id = 0;
        Uri result;
        switch (uriType) {
            case MESSAGE:
                id = sqlDB.insert(CacheDao.TABLE_NAME, null, values);
                result = Uri.parse(CONTENT_URI_MESSAGE + "/" + id);
                break;
            case CRV:
                id = sqlDB.insert(CacheDao.TABLE_NAME_VISIT_REPORT, null, values);
                result = Uri.parse(CONTENT_URI_CRV + "/" + id);
                break;
            case CALL:
                new Insert<CallEndedEvent>(CallEndedEvent.class).columns(String.valueOf(CallEndedEvent_Table.id)).values(id).query();
                result = Uri.parse(CONTENT_URI_CALL + "/" + id);
                break;
            case CONTACT:
                new Insert<Contact>(Contact.class).columns(String.valueOf(Contact_Table.phoneNumber)).values(id).query();
                result = Uri.parse(CONTENT_URI_CONTACT + "/" + id);
                break;
            case EVENT:
                new Insert<Event>(Event.class).columns(String.valueOf(Event_Table.id)).values(id).query();
                result = Uri.parse(CONTENT_URI_EVENT + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public boolean onCreate() {
        dataBase = new OrmTabDataBase();
        mDatabase = new CacheDao(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MESSAGE:
                queryBuilder.setTables(CacheDao.TABLE_NAME);
                queryBuilder.appendWhere("id=" + uri.getLastPathSegment());
                Cursor cursor1 = queryBuilder.query(mDatabase.getWritableDatabase(), projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor1.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor1;
            case CRV:
                queryBuilder.setTables(CacheDao.TABLE_NAME_VISIT_REPORT);
                queryBuilder.appendWhere("id=" + uri.getLastPathSegment());
                Cursor cursor2 = queryBuilder.query(mDatabase.getWritableDatabase(), projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor2.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor2;
            case CALL:
                new Select().from(CallEndedEvent.class).where(CallEndedEvent_Table.id.eq(Long.parseLong(uri.getLastPathSegment()))).querySingle();
                return null;
            case CONTACT:
                new Select().from(Contact.class).where(Contact_Table.phoneNumber.eq((uri.getLastPathSegment()))).querySingle();
                return null;
            case EVENT:
                new Select().from(Event.class).where(Event_Table.id.eq(Long.valueOf((uri.getLastPathSegment())))).querySingle();
                return null;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase database = mDatabase.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        int count = 0;
        switch (uriType) {
            case MESSAGE:
                count = database.update(CacheDao.TABLE_NAME, values, "id=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;
            case CRV:
                count = database.update(CacheDao.TABLE_NAME_VISIT_REPORT, values, "id=" + uri.getLastPathSegment() + " and " + selection, selectionArgs);
                break;
            case CALL:
                new Update<CallEndedEvent>(CallEndedEvent.class).set().where(CallEndedEvent_Table.id.eq(Long.parseLong(uri.getLastPathSegment()))).query();
                break;
            case CONTACT:
                new Update<Contact>(Contact.class).set().where(Contact_Table.phoneNumber.eq(uri.getLastPathSegment())).query();
                break;
            case EVENT:
                new Update<Event>(Event.class).set().where(Event_Table.id.eq(Long.parseLong(uri.getLastPathSegment()))).query();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
